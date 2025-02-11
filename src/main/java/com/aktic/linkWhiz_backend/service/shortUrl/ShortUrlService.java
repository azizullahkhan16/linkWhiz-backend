package com.aktic.linkWhiz_backend.service.shortUrl;

import com.aktic.linkWhiz_backend.model.entity.AnalyticsUrl;
import com.aktic.linkWhiz_backend.model.entity.ShortUrl;
import com.aktic.linkWhiz_backend.model.entity.User;
import com.aktic.linkWhiz_backend.model.request.ShortUrlRequest;
import com.aktic.linkWhiz_backend.model.request.ShortUrlResponse;
import com.aktic.linkWhiz_backend.repository.AnalyticsUrlRepository;
import com.aktic.linkWhiz_backend.repository.ShortUrlRepository;
import com.aktic.linkWhiz_backend.service.fileStorage.FileStorageService;
import com.aktic.linkWhiz_backend.service.hashing.HashingService;
import com.aktic.linkWhiz_backend.service.qrCode.QRCodeService;
import com.aktic.linkWhiz_backend.util.ApiResponse;
import com.aktic.linkWhiz_backend.util.SnowflakeIdGenerator;
import com.aktic.linkWhiz_backend.util.ValidationCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigInteger;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final HashingService hashingService;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final AnalyticsUrlRepository analyticsUrlRepository;
    private final QRCodeService qrCodeService;
    private final ValidationCheck validationCheck;
    private final FileStorageService fileStorageService;

    public ResponseEntity<ApiResponse<ShortUrlResponse>> shortenUrl(ShortUrlRequest shortUrlRequest) {
        try {
            // Get authenticated user directly
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            // Check if user already shortened this URL
            Optional<ShortUrl> existingShortUrl = shortUrlRepository.findByOriginalUrlAndUser(shortUrlRequest.getOriginalUrl(), user);
            if (existingShortUrl.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Shortened URL retrieved successfully", new ShortUrlResponse(existingShortUrl.get())));
            }

            // Validate expiresAt: Must be in the future and converted to UTC
            Instant nowUtc = Instant.now();
            Instant expiresAtUtc = shortUrlRequest.getExpiresAt() != null
                    ? shortUrlRequest.getExpiresAt().atZone(ZoneOffset.UTC).toInstant()
                    : null;

            if (expiresAtUtc != null && expiresAtUtc.isBefore(nowUtc)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Expiration time must be in the future", null));
            }

            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().replacePath("").toUriString();
            String fullShortUrl;
            // Generate unique ID and hash only when needed
            Long uniqueId = snowflakeIdGenerator.nextId();

            if (shortUrlRequest.getCustomAlias() != null) {
                // Check if custom alias already exists
                if (shortUrlRepository.existsByCustomAlias(shortUrlRequest.getCustomAlias())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiResponse<>(false, "Custom alias already exists", null));
                }
                fullShortUrl = baseUrl + "/" + shortUrlRequest.getCustomAlias();
            } else {

                fullShortUrl = baseUrl + "/" + hashingService.hash(uniqueId);
            }

            // Create and save new short URL
            ShortUrl newShortUrl = ShortUrl.builder()
                    .id(uniqueId)
                    .originalUrl(shortUrlRequest.getOriginalUrl())
                    .shortUrl(fullShortUrl)
                    .customAlias(shortUrlRequest.getCustomAlias())
                    .user(user)
                    .expiresAt(expiresAtUtc)
                    .build();

            newShortUrl = shortUrlRepository.save(newShortUrl);

            // create analytics url
            AnalyticsUrl analyticsUrl = AnalyticsUrl.builder()
                    .id(snowflakeIdGenerator.nextId())
                    .clicks(BigInteger.ZERO)
                    .shortUrl(newShortUrl)
                    .build();

            analyticsUrlRepository.save(analyticsUrl);

            return ResponseEntity.ok(new ApiResponse<>(true, "URL shortened successfully", new ShortUrlResponse(newShortUrl)));

        } catch (Exception e) {
            log.error("Unexpected error occurred while shortening URL", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }

    public ResponseEntity<ApiResponse<ShortUrlResponse>> generateQRCode(Long shortUrlId, MultipartFile image, Integer _width, Integer _height, String _onColor, String _offColor) {
        try {
            // Get authenticated user directly
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            Optional<ShortUrl> shortUrlOptional = shortUrlRepository.findByIdAndUserId(shortUrlId, user.getId());
            if (shortUrlOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Short URL not found", null));
            }

            Integer height = _height != null ? _height : 300;
            if (height > 300 || height < 100) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Height must be between 100 and 300", null));
            }

            Integer width = _width != null ? _width : 300;
            if (width > 300 || width < 100) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Width must be between 100 and 300", null));
            }

            String onColor = _onColor != null ? _onColor : "0xFF000000";
            String offColor = _offColor != null ? _offColor : "0xFFFFFFFF";
            System.out.println(onColor);
            System.out.println(offColor);
            if (!validationCheck.isValidHexColor(onColor) || !validationCheck.isValidHexColor(offColor)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Invalid color format", null));
            }

            String fileName;
            ShortUrl shortUrl = shortUrlOptional.get();

            if (image == null) {
                fileName = qrCodeService.generateQRCodeToFile(shortUrl.getShortUrl(), width, height, validationCheck.convertHexToInt(onColor), validationCheck.convertHexToInt(offColor), shortUrl.getId());
            } else {
                fileName = qrCodeService.generateQRCodeWithImage(shortUrl.getShortUrl(), width, height, validationCheck.convertHexToInt(onColor), validationCheck.convertHexToInt(offColor), shortUrl.getId(), image);
            }

            shortUrl.setQrCode(fileName);

            shortUrlRepository.save(shortUrl);

            return ResponseEntity.ok(new ApiResponse<>(true, "QR code generated successfully", new ShortUrlResponse(shortUrl)));


        } catch (Exception e) {
            log.error("Unexpected error occurred while generating QR code", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }

    public ResponseEntity<Resource> getQRCode(Long urlId) {
        try {
            // Get authenticated user directly
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            Optional<ShortUrl> shortUrlOptional = shortUrlRepository.findByIdAndUserId(urlId, user.getId());
            if (shortUrlOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .build();
            }

            ShortUrl shortUrl = shortUrlOptional.get();
            System.out.println(shortUrl.getQrCode());
            Resource resource = fileStorageService.load(shortUrl.getQrCode());

            if (resource == null || !resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Detect the content type dynamically
            String contentType = Files.probeContentType(resource.getFile().toPath());

            if (contentType == null) {
                contentType = "application/octet-stream"; // Fallback for unknown files
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + user.getImage() + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("Unexpected error occurred while getting QR code", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public ResponseEntity<ApiResponse<ShortUrlResponse>> getShortUrl(Long shortUrlId) {
        try {
            // Get authenticated user directly
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            Optional<ShortUrl> shortUrlOptional = shortUrlRepository.findByIdAndUserId(shortUrlId, user.getId());
            if (shortUrlOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Short URL not found", null));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Short URL retrieved successfully", new ShortUrlResponse(shortUrlOptional.get())));

        } catch (Exception e) {
            log.error("Unexpected error occurred while getting short URL", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }

    public ResponseEntity<ApiResponse<Page<ShortUrlResponse>>> getShortUrls(
            Integer pageNumber, Integer limit, String createdAt, String expiresAt) {
        try {
            // Get authenticated user directly
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            Long userId = user.getId();

            int page = (pageNumber != null && pageNumber > 0) ? pageNumber : 0; // Page index starts at 0
            int size = (limit != null && limit > 0) ? limit : 10;

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Instant createdAtStart = null, createdAtEnd = null; // Defaults
            Instant expiresAtStart = null, expiresAtEnd = null;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // Parse `createdAt` to a date (ignoring time)
            if (createdAt != null && !createdAt.isEmpty()) {
                try {
                    LocalDate createdDate = LocalDate.parse(createdAt, formatter);
                    createdAtStart = createdDate.atStartOfDay(ZoneOffset.UTC).toInstant();
                    createdAtEnd = createdDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
                } catch (DateTimeParseException e) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>(false, "Invalid createdAt format. Use YYYY-MM-DD", null));
                }
            }

            if (expiresAt != null && !expiresAt.isEmpty()) {
                try {
                    LocalDate expiresDate = LocalDate.parse(expiresAt, formatter);
                    expiresAtStart = expiresDate.atStartOfDay(ZoneOffset.UTC).toInstant();
                    expiresAtEnd = expiresDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
                } catch (DateTimeParseException e) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>(false, "Invalid expiresAt format. Use YYYY-MM-DD", null));
                }
            }

            // Fetch filtered short URLs
            Page<ShortUrl> shortUrls;

            if (createdAt != null && expiresAt != null) {
                shortUrls = shortUrlRepository.findByUserIdAndCreatedAtBetweenAndExpiresAtBetween(userId, createdAtStart, createdAtEnd, expiresAtStart, expiresAtEnd, pageable);
            } else if (createdAt != null) {
                shortUrls = shortUrlRepository.findByUserIdAndCreatedAtBetween(userId, createdAtStart, createdAtEnd, pageable);
            } else if (expiresAt != null) {
                shortUrls = shortUrlRepository.findByUserIdAndExpiresAtBetween(userId, expiresAtStart, expiresAtEnd, pageable);
            } else {
                shortUrls = shortUrlRepository.findByUserId(userId, pageable);
            }

            // Convert entity to response DTO
            Page<ShortUrlResponse> responsePage = shortUrls.map(ShortUrlResponse::new);

            return ResponseEntity.ok(new ApiResponse<>(true, "Short URLs fetched successfully", responsePage));

        } catch (Exception e) {
            log.error("Unexpected error occurred while getting short URLs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Internal server error", null));
        }
    }


}

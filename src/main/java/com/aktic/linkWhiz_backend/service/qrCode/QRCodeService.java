package com.aktic.linkWhiz_backend.service.qrCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;

@Service
@Slf4j
public class QRCodeService {

    private static final String UPLOADS_DIR = "uploads";

    public String generateQRCodeToFile(String url, int width, int height, int onColor, int offColor, Long urlId) throws WriterException, IOException {
        // Ensure the uploads directory exists
        Path uploadPath = Paths.get(UPLOADS_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate a unique filename using the timestamp and provided identifier
        String fileName = System.currentTimeMillis() + "_" + urlId + ".png";
        Path filePath = uploadPath.resolve(fileName);

        // QR Code parameters
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // High error correction
        hints.put(EncodeHintType.MARGIN, 2); // Adjust margin (default is 4)

        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height, hints);

        // Custom colors (Foreground: Black, Background: White)
        MatrixToImageConfig config = new MatrixToImageConfig(onColor, offColor);

        // Write QR Code to file
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath, config);

        return fileName;
    }

    public String generateQRCodeWithImage(String url, int width, int height, int onColor, int offColor, Long urlId, MultipartFile logoFile) throws WriterException, IOException {
        // Ensure the uploads directory exists
        Path uploadPath = Paths.get(UPLOADS_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate a unique filename
        String fileName = System.currentTimeMillis() + "_" + urlId + ".png";
        Path filePath = uploadPath.resolve(fileName);

        // QR Code parameters
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // High error correction
        hints.put(EncodeHintType.MARGIN, 2); // Adjust margin (default is 4)

        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height, hints);

        // Convert BitMatrix to BufferedImage
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig(onColor, offColor));

        // Check if a logo file is provided
        if (logoFile != null && !logoFile.isEmpty()) {
            try (InputStream logoStream = logoFile.getInputStream()) {
                BufferedImage logoImage = ImageIO.read(logoStream);

                // Calculate the position for the logo (center)
                int overlayWidth = width / 3;  // Logo size = 1/3rd of QR code
                int overlayHeight = height / 3;
                int overlayX = (width - overlayWidth) / 2;
                int overlayY = (height - overlayHeight) / 2;

                // Scale the logo image
                Image scaledLogo = logoImage.getScaledInstance(overlayWidth, overlayHeight, Image.SCALE_SMOOTH);
                BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) combined.getGraphics();

                // Draw the QR code
                g.drawImage(qrImage, 0, 0, null);

                // Draw the logo on top
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.drawImage(scaledLogo, overlayX, overlayY, null);
                g.dispose();

                // Save the final QR code with the logo
                ImageIO.write(combined, "PNG", filePath.toFile());
            }
        } else {
            // Save the QR code without the logo
            ImageIO.write(qrImage, "PNG", filePath.toFile());
        }

        return fileName;  // Return the filename of the saved QR code
    }
}
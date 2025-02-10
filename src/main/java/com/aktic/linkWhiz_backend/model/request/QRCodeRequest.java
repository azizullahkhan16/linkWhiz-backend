package com.aktic.linkWhiz_backend.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QRCodeRequest {
    @Min(value = 100, message = "Height must be at least 100")
    @Max(value = 300, message = "Height must not be more than 300")
    private Integer height = 300;

    @Min(value = 100, message = "Width must be at least 100")
    @Max(value = 300, message = "Width must not be more than 300")
    private Integer width = 300;

    @Pattern(regexp = "^0x[0-9A-Fa-f]{8}$", message = "offColor must be a valid ARGB hex value (e.g., 0xFFFFFFFF)")
    private String offColor = "0xFFFFFFFF";

    @Pattern(regexp = "^0x[0-9A-Fa-f]{8}$", message = "onColor must be a valid ARGB hex value (e.g., 0xFF000000)")
    private String onColor = "0xFF000000";

    private MultipartFile image;

}

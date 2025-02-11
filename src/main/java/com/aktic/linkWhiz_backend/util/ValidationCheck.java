package com.aktic.linkWhiz_backend.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidationCheck {

    private static final String HEX_COLOR_REGEX = "^0x[0-9A-Fa-f]{8}$";
    private static final Pattern HEX_PATTERN = Pattern.compile(HEX_COLOR_REGEX);

    public boolean isValidHexColor(String color) {
        if (color == null || color.isBlank()) {
            return false; // Return false if null or empty
        }
        return HEX_PATTERN.matcher(color).matches();
    }

    public int convertHexToInt(String hexColor) {
        if (hexColor == null || hexColor.isBlank()) {
            throw new IllegalArgumentException("Color value cannot be null or empty");
        }

        // Remove "0x" prefix and parse as long (base 16)
        return (int) Long.parseLong(hexColor.replace("0x", ""), 16);
    }
}

package com.aktic.linkWhiz_backend.constant;

public class SecurityConstants {

    // API Paths to bypass authentication in the filter
    public static final String[] FILTER_BYPASS_PATHS = {
            "/api/auth",
            "/api/plan"
    };

    // White-listed URLs that do not require authentication
    public static final String[] WHITE_LIST_URL = {
            "/api/auth/**",
            "/api/plan/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
}
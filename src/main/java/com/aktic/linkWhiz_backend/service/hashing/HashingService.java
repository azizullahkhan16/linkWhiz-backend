package com.aktic.linkWhiz_backend.service.hashing;

import org.springframework.stereotype.Service;

@Service
public class HashingService {

    private static final String BASE62_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = BASE62_ALPHABET.length();

    public String hash(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("ID must be a non-negative number");
        }

        StringBuilder encoded = new StringBuilder();
        while (id > 0) {
            int remainder = (int) (id % BASE);
            encoded.append(BASE62_ALPHABET.charAt(remainder));
            id /= BASE;
        }

        return encoded.reverse().toString();
    }
}

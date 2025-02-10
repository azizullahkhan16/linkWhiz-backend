package com.aktic.linkWhiz_backend.controller;

import com.aktic.linkWhiz_backend.service.redirect.RedirectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class RedirectController {

    private final RedirectService redirectService;

    @GetMapping("/{alias}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String alias) {
        return redirectService.redirectToOriginalUrl(alias);
    }

    @GetMapping("/")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Welcome to LinkWhiz");
    }
}

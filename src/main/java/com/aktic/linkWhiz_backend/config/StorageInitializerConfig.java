package com.aktic.linkWhiz_backend.config;

import com.aktic.linkWhiz_backend.service.fileStorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorageInitializerConfig implements ApplicationRunner {

    private final FileStorageService storageService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        storageService.init();
    }
}

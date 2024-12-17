package com.poc_rabbit.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Configuration
public class FileStorageConfig {

    @Value("${app.file.upload.dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try{
            Files.createDirectories(Path.of(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar a pasta: ", e);
        }
    }
}

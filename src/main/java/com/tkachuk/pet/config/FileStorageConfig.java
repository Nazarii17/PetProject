package com.tkachuk.pet.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@Slf4j
public class FileStorageConfig {

    @Value("${upload.path}")
    private String baseDir;

    @Value("${upload.user.path}")
    private String userBaseDir;

    @Bean(name = "basePath")
    public String basePath() {
        //TODO File baseDir = new File(baseDir); ?????
        File baseDir = new File("uploads");
        if (!baseDir.exists()) {
            baseDir.mkdir();
        }
        log.info("BasePath created: ", baseDir.getAbsolutePath());
        return baseDir.getAbsolutePath();
    }

    @Bean(name = "userBasePath")
    public String userBasePath() {
        File baseDir = new File(userBaseDir);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
            log.info("userBasePath '" + userBaseDir + "' created: ", baseDir.getAbsolutePath());
        }
        return baseDir.getAbsolutePath();
    }
}

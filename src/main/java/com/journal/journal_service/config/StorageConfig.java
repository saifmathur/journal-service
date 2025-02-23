package com.journal.journal_service.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;


@Configuration
public class StorageConfig {

    private static final Logger log = LoggerFactory.getLogger(StorageConfig.class);

    @Value("${S3_ACCESS_KEY}")
    String access_key;

    @Value("${S3_SECRET_KEY}")
    String secret_key;

    @Value("${S3_URL}")
    String url;


    @Bean
    public S3Client s3Client() throws Exception {
        try {
            return S3Client.builder().credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(access_key, secret_key))).region(Region.US_WEST_1) // Supabase does not use region, but AWS SDK requires it
                    .endpointOverride(URI.create(url)).serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true) // Required for non-AWS S3 storage
                            .build()).build();

        } catch (Exception e) {
            log.info("S3 ERROR::");
            throw new Exception(e);
        }
    }

}

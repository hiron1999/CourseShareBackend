package com.example.AwsService.configration;

import java.util.function.BiFunction;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.HttpCredentialsProvider;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.core.internal.http.loader.DefaultSdkHttpClientBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.internal.s3express.DefaultS3ExpressIdentityProvider;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Configuration
public class AwsConfig {
     @Value("${aws.accessKeyId}")
    private String ACCESS_KEY;
    @Value("${aws.secretkey}")
    private String SECRET_KEY;


    @Value("${aws.s3.region}")
    private String REGION;

    @PostConstruct
    public void setSystemProperty() {
//
//        System.setProperty("aws.accessKeyId", ACCESS_KEY);
//        System.setProperty("aws.secretAccessKey", SECRET_KEY);
    }

    @Bean
    public AwsCredentialsProvider credentialsProvider(){

        AwsCredentials credentials=AwsBasicCredentials.create(ACCESS_KEY,SECRET_KEY);
        System.out.println("credentials>>>>>>>>>>>>>>>>>>>>>>"+credentials.accessKeyId() +":"+credentials.secretAccessKey());
        AwsCredentialsProvider awsCredentialsProvider=new AwsCredentialsProvider() {
            @Override
            public AwsCredentials resolveCredentials() {
                return credentials;
            }
        };
        return awsCredentialsProvider;
    }
    @Bean
    public S3Client s3Client(){
       return S3Client.builder()

               .credentialsProvider(credentialsProvider())
                .region(Region.AP_SOUTH_1)
                .build();
    }

    @Bean
    public S3AsyncClient s3AsyncClient(){
        return S3AsyncClient.builder()
                 .credentialsProvider(credentialsProvider())
                 .region(Region.AP_SOUTH_1)
                 .build();
    }




}

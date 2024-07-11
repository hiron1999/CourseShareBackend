package com.example.AwsService.util;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Map;

public class S3StorageRequest {
    public static GetObjectRequest getObjectRequest(String bucket, String key){

        return  GetObjectRequest
                .builder()
                .bucket(bucket)
                .key(key)
                .build();
    }

    public static PutObjectRequest putObjectRequest (String bucket, String key, Map<String,String> metadata){
        return PutObjectRequest
                .builder()
                .bucket(bucket)
                .key(key)
                .metadata(metadata)
                .build();
    }
}

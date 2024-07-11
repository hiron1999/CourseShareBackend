package com.example.AwsService.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.io.File;

public interface ResourceStorageService {

    public Mono<String> saveResource (Mono<FilePart> filepart, String mappingId) ;
    public Mono<File> getResource(String mappingId);
}

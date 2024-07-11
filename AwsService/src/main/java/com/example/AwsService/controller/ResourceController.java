package com.example.AwsService.controller;

import com.example.AwsService.service.ResourceStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("resourse/")
@Slf4j
public class ResourceController {
    @Autowired
    private ResourceStorageService resourceStorageService;

    @PostMapping(value = "/upload/{mappingId}" ,consumes = "multipart/form-data" )
    public Mono<ResponseEntity<String>> uploadResource(@RequestPart("file")Mono<FilePart> filePartMono, @PathVariable String mappingId){

        return resourceStorageService.saveResource(filePartMono,mappingId)
                .map(res -> ResponseEntity.status(HttpStatus.CREATED).body(res))
                .onErrorResume(err -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getLocalizedMessage())));
    }

}

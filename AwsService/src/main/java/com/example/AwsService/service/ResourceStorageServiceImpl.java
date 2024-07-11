package com.example.AwsService.service;

import com.example.AwsService.constaints.AWSResourceConstant;
import com.example.AwsService.entity.AWSResourceEntity;
import com.example.AwsService.util.AWSutil;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@Slf4j
public class ResourceStorageServiceImpl implements ResourceStorageService{
    private static final String DIRECTORY = "VedioCache/";
    @Autowired
    private DatabaseManagementService dbsservice;
    @Autowired
    private AwsS3Service awsS3Service;
    @Override
    public Mono<String> saveResource(Mono<FilePart> filepart, String mappingId)  {
        AWSResourceEntity resource =getReuestedResource(mappingId);
        if(resource!=null) {
            return filepart.doOnNext(filePart -> System.out.println("Recieving file : " + filePart.filename()))
                    .flatMap(
                    fp -> {
                        String filename = fp.filename();
                        File recievedfile = new File(String.format("uploads/%s", filename));

                        return fp.transferTo(Paths.get(recievedfile.getAbsolutePath()))
                                .doOnSuccess(id -> log.info("File saved at : {}", recievedfile.getAbsolutePath()))
                                .doOnError(error -> log.error("Failed to save file", error))
                                .then(Mono.fromCallable(
                                        ()->{
                                            String mimeType = Files.probeContentType(recievedfile.toPath());
                                            log.info("recieved file type -> {}", mimeType);
                                            String resourceType = resource.getType();

                                            if (!AWSutil.isAllowedFileType(mimeType, resourceType)) {
                                              throw new IllegalArgumentException("resource type mismatch! required type:" + resourceType);

                                            }
                                            // upload the file
                                            String storagepath = AWSutil.getResourceLocation(resource);

                                            return  awsS3Service.putFile(recievedfile, resourceType, storagepath);

                                        }).flatMap(res-> {return res;}))
                                .doOnSuccess(res->{
                                    dbsservice.updateResourceStatus(resource.getResourceId(),AWSResourceConstant.RESOURCE_STATUS_AVAILABLE);
                                })
                                .onErrorMap(e -> new Throwable("File upload failed :"+e.toString()))
                                .doFinally(signalType -> {
                                    try {
                                        Files.deleteIfExists(recievedfile.toPath());
                                    } catch (IOException e) {
                                        e.fillInStackTrace();
                                    }
                                });

                    }
            );
        }else {
           return Mono.error( new IllegalArgumentException("resource is not found"));

        }
    }
    @Override
    public Mono<File> getResource(String mappingId){
        log.info("Get file from cache---> {}", mappingId);
        File file = new File(DIRECTORY+mappingId);
//        if (!file.isFile()) {
////            awsS3Service.saveFile(title);
//            log.info("file not found call s3 client.........");
//
//        }
        return Mono.fromCallable(()->{
            if (file.isFile()) {
                return Mono.just(file);
            }
            else {
                log.info("file not found in local cache , fetching from cloude storage ----->");
                AWSResourceEntity resource =getReuestedResource(mappingId);
                if (!Objects.equals(resource.getStatus(), AWSResourceConstant.RESOURCE_STATUS_AVAILABLE)){
                   throw  new NotFoundException("Resource not avaliable");
                }
                String resourceKey = AWSutil.getResourceLocation(getReuestedResource(mappingId));
                String localStoragePath = DIRECTORY+mappingId;
                return awsS3Service.saveFile(localStoragePath,resourceKey)
                        .onErrorMap(Throwable::new);

            }
        }).flatMap(response-> {return response;})
        .onErrorMap(Throwable::new);

    }

    private AWSResourceEntity getReuestedResource(String mappingId) {
        return dbsservice.getAwsResourceByCourseResourceId(mappingId);
    }
}

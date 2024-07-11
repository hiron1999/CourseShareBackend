package com.example.AwsService.service;



import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.nio.file.Path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


import com.example.AwsService.util.S3StorageRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;

import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Slf4j
@Service
public class AwsS3Service {
    private static final String DIRECTORY="VedioCache/%s.mp4";
    private static final String KEY="vedios/%s.mp4";
    private static final long THRESOLD = 1024 * 1024;
    @Value("${aws.s3.bucketname}")
    private String BUCKET_NAME;
//    @Value("download/")
    private String BasePath;
    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3AsyncClient s3AsyncClient;

    @Autowired
    private DatabaseManagementService databaseManagementService;



    public List<String > getBucketList(){
        return s3Client.listBuckets().buckets().stream().map(Bucket::name).collect(Collectors.toList());
    }


    public void getFile(String filename){

        ResponseInputStream<GetObjectResponse> objectBytes =s3Client.getObject(S3StorageRequest.getObjectRequest(BUCKET_NAME,filename));
//
        File myfile= new File(filename);
        System.out.println("new file ----------------->"+ myfile.getAbsolutePath());

        try(OutputStream os=new FileOutputStream(myfile)) {
            byte[] bytes=objectBytes.readAllBytes();
            os.write(bytes);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            System.out.println("file download at path :"+ BasePath);
        }

    }

    public Mono<File> saveFile(String filepath,String key){

//
//        File file=new File(filepath);
//            file.createNewFile();
//        CompletableFuture<GetObjectResponse> future=s3AsyncClient
//                .getObject(S3StorageRequest.getObjectRequest(BUCKET_NAME,key),
//                        AsyncResponseTransformer.toFile(Path.of(file.getAbsolutePath())));
//
//
//        return Mono.just(future).map(fu->{
//            while (!future.isDone()){
//                log.info("file lenth: {}", file.length());
//                if(file.length()> THRESOLD){
//                    return file;
//                }
//            }
//
//        });

        return Mono.fromCallable(() -> {
            File file = new File(filepath);
            file.createNewFile(); // Create an empty file to receive the S3 object

            CompletableFuture<GetObjectResponse> future = s3AsyncClient.getObject(
                    S3StorageRequest.getObjectRequest(BUCKET_NAME, key),
                    AsyncResponseTransformer.toFile(Path.of(file.getAbsolutePath())));

            CompletableFuture<File> processingFuture = CompletableFuture.supplyAsync(() -> {
                while (!future.isDone()) {
                    if(future.isCancelled()) {
                        break;
                    }
//                    log.info("File length: {}", file.length());
                    if (file.length() >= THRESOLD) {
                        log.info("Threshold reached, starting processing...");

                        return file;
                    }
                }
                return file;
            });

            // Return a Mono that completes when processingFuture completes
            return processingFuture.join();
        })
        .onErrorMap(err -> new Throwable("Unable to get file : {}",err));


    }



    public Mono<String> putFile(File file , String type, String key){
            log.info("inside PutFile()");

            Map<String ,String > metadata =new HashMap<>();
            metadata.put("name",file.getName());
            metadata.put("type",type);
            PutObjectRequest putObjectRequest = S3StorageRequest.putObjectRequest(BUCKET_NAME,key,metadata);

            CompletableFuture<PutObjectResponse> future=s3AsyncClient
                    .putObject(putObjectRequest, AsyncRequestBody.fromFile(file));
        return Mono.fromFuture( future.whenComplete((res,err)->{
                if(res!=null){

                log.info("upload resource response : {}", res.toString());

                }else{
                    log.info("failed to upload resource : {}", err.toString());
                    throw new RuntimeException(err);
                }

            })
                .thenApply(PutObjectResponse::toString)
        ).onErrorMap(Throwable::new);


    }




}

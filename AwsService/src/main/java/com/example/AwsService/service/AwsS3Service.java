package com.example.AwsService.service;

import static java.rmi.server.LogStream.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.common.reflect.ClassPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.internal.async.InputStreamWithExecutorAsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Slf4j
@Service
public class AwsS3Service {
    private static final String DIRECTORY="VedioCache/%s.mp4";
    private static final String KEY="vedios/%s.mp4";
    @Value("${aws.s3.bucketname}")
    private String BUCKET_NAME;
//    @Value("download/")
    private String BasePath;
    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3AsyncClient s3AsyncClient;


    public List<String > getBucketList(){
        return s3Client.listBuckets().buckets().stream().map(Bucket::name).collect(Collectors.toList());
    }


    public void getFile(String filename){

        ResponseInputStream<GetObjectResponse> objectBytes =s3Client.getObject(getObjectRequest(BUCKET_NAME,filename));
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

    public void saveFile(String filename){

        try {
        File file=new File(String.format(DIRECTORY,filename));
            file.createNewFile();
        CompletableFuture<GetObjectResponse> future=s3AsyncClient
                .getObject(getObjectRequest(BUCKET_NAME,String.format(KEY,filename)),
                        AsyncResponseTransformer.toFile(Path.of(file.getAbsolutePath())));
        while (!future.isDone()){
            log.info("file lenth: "+file.length());
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



//        future.whenComplete((res,err)->{
//            try(OutputStream os=new FileOutputStream(file)){
//                os.write(res.readAllBytes());
//
//            log.info("file length initial"+file.length());
//
//            }catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        future.join();

//        try (ResponseInputStream<GetObjectResponse> responseStream = future.join()) {
//        log.info("response lenth:{}/{} ",responseStream.readAllBytes().length,responseStream.response().contentRange());  // BLOCKS the calling thread
//
//     } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        log.info("file length final"+file.length());

    }

    private GetObjectRequest getObjectRequest(String bucket,String key){

       return  GetObjectRequest
                .builder()
                .bucket(bucket)
                .key(key)
                .build();
    }

}

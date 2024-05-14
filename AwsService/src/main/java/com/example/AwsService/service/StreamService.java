package com.example.AwsService.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StreamService {

    private static final String DIRECTORY = "VedioCache/%s.mp4";
    private static final long BUFFER_SIZE = 1024 * 50;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private AwsS3Service awsS3Service;

    public ResponseEntity<byte[]> getVideo(String title, String range) {

//        return Mono.fromSupplier(()->resourceRegion(
//                resourceLoader.getResource(String.format(FORMAT,title)),range.split("=")[1]));

        File file = new File(String.format(DIRECTORY, title));
        if (!file.isFile()) {
//            awsS3Service.saveFile(title);
            log.info("file not found call s3 client.........");
        }

        String[] ranges =range.split("=")[1].split("-");
        long start_range = 0;
        long end_range = 0;
        if (!range.isEmpty()) {

            start_range = ranges.length > 0 ? Long.parseLong(ranges[0]) : start_range;
//            end_range = ranges.length > 1 ? Long.parseLong(ranges[1]) : start_range + 1024;
        }

        byte[] data= getContentByRange(file,  start_range);
        end_range =start_range+data.length-1;

        ResponseEntity<byte[]> response;
        if (data != null) {
            response = ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Accept-Ranges", "bytes")
                    .header("Content-Range", "bytes" + " " + start_range + "-" + end_range + "/" + file.length())
                    .body(data);
        } else {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    private byte[] getContentByRange(File file, long start_range) {
        log.info("inside getContentByRange...........");


        long content_length = file.length();

        byte[] data = new byte[Math.toIntExact(Math.min(content_length - start_range, BUFFER_SIZE))];
        try (FileInputStream input = new FileInputStream(file)) {

            FileChannel source = input.getChannel();
            ByteBuffer buffer = ByteBuffer.wrap(data, 0, data.length);
            source.position(start_range);
            if (source.read(buffer) == -1) {
//                buffer.flip();
                log.info("unable to read......");
                return null;
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("fetced data :" + data.length);
        return data;

    }

    private ResourceRegion resourceRegion(Resource media, String range) {
        long contentLength = 0;
        try {
            contentLength = media.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("range------------->" + range);
//    List<String> range = headers.getRange();
        if (!range.isEmpty()) {
            String[] ranges = range.split("-");
            long start = Long.parseLong(ranges[0]);
            long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : contentLength - 1;
            long rangeLength = Math.min(end - start + 1, BUFFER_SIZE);
            return new ResourceRegion(media, start, rangeLength);
        } else {
            long rangeLength = Math.min(1024 * 1024, contentLength);
            return new ResourceRegion(media, 0, rangeLength);
        }
    }
}

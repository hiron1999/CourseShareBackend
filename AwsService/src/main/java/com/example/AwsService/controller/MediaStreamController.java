package com.example.AwsService.controller;

import com.example.AwsService.service.StreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("media/")
@Slf4j
public class MediaStreamController {

	@Autowired
	private StreamService streamService;

	@GetMapping(value = "/{title}", produces = "video/mp4")
	public Mono<ResponseEntity<byte[]>> getVedio(@PathVariable String title, @RequestHeader("Range") String range) {
//		System.out.println("renge in header :" + range.toString());
//		return streamService.getVideo(title,range);
		log.info("renge in header :" + range);
		return Mono.just(streamService.getVideo(title, range));

	}

}

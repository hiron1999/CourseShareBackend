package com.example.AwsService;

import java.util.List;

import com.example.AwsService.service.AwsS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
@EnableDiscoveryClient
@RestController
@SpringBootApplication
public class AwsServiceApplication {

		@Value("classpath:download/")
		public String BasePath;
	@Value("${aws.accessKeyId}")
	private String ACCESS_KEY;
	@Value("aws.secretkey")
	private String SECRET_KEY;

//	@PostConstruct
//	public void setSystemProperty() {
//
//		System.setProperty("aws.accessKeyId", ACCESS_KEY);
//		System.setProperty("aws.secretAccessKey", SECRET_KEY);
//	}

	@Autowired
	private AwsS3Service awsS3Service;
	@GetMapping("/lists3")
	public List<String> getFiles(){
		return awsS3Service.getBucketList();
	}
//	@GetMapping("/file/{name}")
//	public void getfile(@PathVariable String name){
//		System.out.println(BasePath);
//		awsS3Service.saveFile(name);
//	}

	public static void main(String[] args) {

		SpringApplication.run(AwsServiceApplication.class, args);
	}

}

package com.courseshare.ApiGetwayService.filter;

import java.nio.charset.StandardCharsets;

import Model.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RegistrationFilter extends AbstractGatewayFilterFactory<RegistrationFilter.Config> {

    private RestTemplate template;


    RegistrationFilter(){
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
       return (exchange, chain) -> {
           ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();


           log.info("incomming  request ---> "+exchange.getRequest().getURI().toString());
           String requestbody= request.getBody().toString();
           return chain.filter(exchange)
                   .then(Mono.fromRunnable(()->{
                       //Post autentication
                      log.info("Response: "+ response.getStatusCode() );
                      if(response.getStatusCode()== HttpStatusCode.valueOf(HttpStatus.CREATED.value())){
                            //Create user object for profile

                          log.info("requestbody :"+requestbody);



//                            user=new UserModel();
//                            user.setEmail(exchange.getRequest().getBody());
                      }
                   }));
       };
    }
    public static class Config {

    }
}

package com.courseshare.ApiGetwayService.util;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationClient {

     private final WebClient webClient;

    @Autowired
    public AuthenticationClient(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.baseUrl("lb://autorization-service").build();
    }

    public Mono<Boolean> validateToken(String token) {



       return webClient.get()

                .uri(uriBuilder -> uriBuilder
                        .path("/autentication/validate")
                        .queryParam("token",token)
                        .build()
                    )
                 .exchangeToMono(
                         clientResponse -> {
                             final Boolean  isValid ;
                             if(clientResponse.statusCode().is2xxSuccessful()){
                                isValid = true;
                             }
                             else {
                                 isValid=false;
                             }
                            return Mono.just(isValid);
                         }
                 );




    }
}

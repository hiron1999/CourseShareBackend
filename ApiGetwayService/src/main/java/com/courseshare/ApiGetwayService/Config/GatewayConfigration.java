package com.courseshare.ApiGetwayService.Config;

import java.util.Arrays;
import java.util.HashMap;

import com.courseshare.ApiGetwayService.filter.AutenticationFilter;
import com.courseshare.ApiGetwayService.filter.RegistrationFilter;
import com.courseshare.ApiGetwayService.util.AuthenticationClient;
import io.netty.handler.codec.http.cors.CorsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
@Slf4j
@Configuration
public class GatewayConfigration {

    @LoadBalanced
    @Bean
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public AuthenticationClient authenticationClient(){
        return new AuthenticationClient(loadBalancedWebClientBuilder());
    }

     @Bean
//     @Order(0)
    public GatewayFilter corsFilter() {

        return (exchange, chain) -> {
            exchange.getResponse().getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            exchange.getResponse().getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponse().getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "authorization, content-type");
            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                log.info("inside....cors filter--------------");
                exchange.getResponse().setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
            return chain.filter(exchange);
        };
    }


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, AutenticationFilter autenticationFilter){
        return builder.routes()
                .route("openApiEndpoint-route",
                        r -> r
                                .method(HttpMethod.OPTIONS,HttpMethod.GET,HttpMethod.POST).and()
                                .path("/auth/token","/auth/register","/auth/v1/token")

                                .filters(f->f

                                        .rewritePath("/auth/(?<remaining>.*)","/autentication/${remaining}")
//                                              .addResponseHeader("Access-Control-Allow-Origin", "*")
//                                            .addResponseHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
//                                            .addResponseHeader("Access-Control-Allow-Headers", "authorization, content-type")
                                        )
                                .uri("lb://autorization-service")


                )
                .route("ContentApiEndpoint-route",
                        r ->r
                                .path("/course/**")
                                .filters(f-> f
//                                             .filter(corsFilter())
//                                        .filter(autenticationFilter.apply(config -> new AutenticationFilter.Config()),0)
                                )
                                .uri("lb://content-service")


                ).route("ActivityApiEndpoint-route",
                        r ->r
                                .path("/course-activity/**")
                                .filters(
                                        f->f
                                                .rewritePath("/course-activity/(?<remaining>.*)","/Activity/${remaining}")
                                )
                                .uri("lb://content-service")


                ).route("UserApiEndpoint-route",
                        r ->r
                                .path("/Profile/**")
                                .filters(f-> f
                                        .filter(autenticationFilter.apply(config -> new AutenticationFilter.Config()),0)
                                )
                                .uri("lb://user-service")


                )
                .route("MediaStreamEndpoint-route",
                        r ->r
                                .path("/video-leacture/**")
                                .filters(
                                        f->f
                                                .rewritePath("/video-leacture/(?<remaining>.*)","/media/${remaining}")
                                )
                                .uri("lb://aws-service")

                        )
                .route("SubscritionApiEndpoint-route",
                        r ->r
                                .path("/subscriptionApi/**")
                                .filters(
                                        f->f
                                                .rewritePath("/subscriptionApi/(?<remaining>.*)","/${remaining}")
                                )
                                .uri("lb://subscription-service")
                        )
                .route("ResourceEndpoint-route",
                        r ->r
                                .path("/`resourse/**")

                                .uri("lb://aws-service")

                )
                .build();
    }
}

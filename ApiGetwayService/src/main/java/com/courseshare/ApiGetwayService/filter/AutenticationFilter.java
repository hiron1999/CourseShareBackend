package com.courseshare.ApiGetwayService.filter;

import com.courseshare.ApiGetwayService.util.AuthenticationClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class AutenticationFilter extends AbstractGatewayFilterFactory<AutenticationFilter.Config> {
    @Autowired
    private AuthenticationClient authenticationClient;
    AutenticationFilter(){
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (excange, chain) -> {
            //header contains token or not
            if (!excange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing authorization header ");
            }

            String authHeader = excange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }
            //calling validate token endpoint
            return authenticationClient.validateToken(authHeader).flatMap(isValid ->
            {
                if (isValid) {
                    //if token is valid pass it to next
                    return chain.filter(excange);
                } else {
                    // for invalid token block request
                    excange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return excange.getResponse().setComplete();
                }
            });


        };
    }

    public static class Config {

    }
}

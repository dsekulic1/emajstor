package com.api.ApiGateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class CheckAdminRole extends AbstractGatewayFilterFactory<CheckAdminRole.Config> {
    @Autowired
    Environment environment;

    public CheckAdminRole(){
        super(CheckAdminRole.Config.class);
    }

    @Override
    public GatewayFilter apply(CheckAdminRole.Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey("Authorization")){
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }
            String authorizationHeader = request.getHeaders().get("Authorization").get(0);
            String jwt = authorizationHeader.replace("Bearer", "");
            if(!isJwtValid(jwt)){
                return onError(exchange, "Jwt token is not valid", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }

    private boolean isJwtValid(String jwt){
        boolean returnValue = true;
        Claims claims = Jwts.parser().setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(jwt).getBody();
        if(claims == null || claims.isEmpty()){
            returnValue = false;
        }
        if(!claims.containsKey("ROLE")){
            return false;
        }
        Integer roleId = (Integer) claims.get("ROLE");
        if(roleId != 1)
            returnValue=false;
        return returnValue;
    }

    public static class Config{
        //put config properties here
    }
}


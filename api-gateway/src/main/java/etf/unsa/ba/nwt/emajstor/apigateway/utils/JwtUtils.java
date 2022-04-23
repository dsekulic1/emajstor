package etf.unsa.ba.nwt.emajstor.apigateway.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class JwtUtils implements Serializable {

    private static int jwtExpirationMilliseconds;
    private static String jwtSecret;

    @Value("${app.jwtExpiration}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        JwtUtils.jwtExpirationMilliseconds = jwtExpirationInMs;
    }

    @Value("${app.jwtSecret}")
    public void setJwtSecret(String jwtSecret) {
        JwtUtils.jwtSecret = jwtSecret;
    }

    public static int getJwtExpirationMilliseconds() {
        return jwtExpirationMilliseconds;
    }

    public static void setJwtExpirationMilliseconds(int jwtExpirationMilliseconds) {
        JwtUtils.jwtExpirationMilliseconds = jwtExpirationMilliseconds;
    }

    public static String getJwtSecret() {
        return jwtSecret;
    }
}
package com.health_care.gateway.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health_care.gateway.config.GatewayDataConfiguration;
import com.health_care.gateway.domain.enums.JwtClaimsEnum;
import com.health_care.gateway.domain.response.CurrentUserContext;
import com.health_care.gateway.exceptions.MissingAuthorizationHeaderException;
import com.health_care.gateway.util.JwtUtil;
import com.health_care.gateway.util.SerializationUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private RouteValidator validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new MissingAuthorizationHeaderException("Missing or invalid Authorization header");
                }

                authHeader = authHeader.substring(7);

                try {
                    JwtUtil.validateToken(authHeader, jwtSecretKey);
                    CurrentUserContext currentUserContext = prepareCurrentContext(authHeader);
                    String jsonCurrentUserContext = toJson(currentUserContext);
                    String base64UserCurrentContext = SerializationUtils.toBase64(
                            jsonCurrentUserContext.getBytes(StandardCharsets.UTF_8)
                    );
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .headers(h -> h.set(GatewayDataConfiguration.CURRENT_USER_CONTEXT_HEADER, base64UserCurrentContext))
                            .build();
                    exchange = exchange.mutate().request(request).build();
                } catch (ExpiredJwtException e) {
                    throw new RuntimeException("Token has expired", e);
                } catch (SignatureException e) {
                    throw new RuntimeException("Invalid token signature", e);
                } catch (Exception e) {
                    logger.warn("Unauthorized access detected: {}", e.getMessage());
                    throw new RuntimeException("Unauthorized access to the application", e);
                }
            }
            return chain.filter(exchange);
        };
    }


    private CurrentUserContext prepareCurrentContext(String token) {
        try {
            String userIdentity = JwtUtil.extractClaimByKey(token, jwtSecretKey, JwtClaimsEnum.USER_IDENTITY.getClaim(), String.class);
            CurrentUserContext currentUserContext = new CurrentUserContext();
            currentUserContext.setUserIdentity(userIdentity);
            return currentUserContext;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private String toJson(CurrentUserContext currentUserContext) {
        String jsonCurrentUserContext = null;
        try {
            jsonCurrentUserContext = objectMapper.writeValueAsString(currentUserContext);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonCurrentUserContext;
    }

    public static class Config {

    }
}

package kz.sw_sku_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class JwtService {

    public String getUsername() {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Map<String, Object> claims = jwt.getClaims();

            if (claims != null) {
                if (claims.containsKey("preferred_username")) {
                    return claims.get("preferred_username").toString();
                }
                if (claims.containsKey("given_name")) {
                    return claims.get("given_name").toString();
                }
            }
        } catch (Exception e) {
            log.info("Unable to get created_by from token");
        }

        return "SYSTEM";
    }
}

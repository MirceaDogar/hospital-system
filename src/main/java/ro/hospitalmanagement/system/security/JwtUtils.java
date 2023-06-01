package ro.hospitalmanagement.system.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${medical.app.jwtSecret}")
    private String jwtSecret;

    @Value("${medical.app.jwtExpirationMs}")
    private int expirationMs;

    public String generateToken(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();


    }
    public String extractUsernameFromJwt(String authToken) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.debug("Invalid JWT signature: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.debug("JWT not supported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.debug("JWT is malformed: {} ", e.getMessage());
        } catch (SignatureException e) {
            log.debug("Invalid signature for JWT : {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.debug("Claim set is empty {}", e.getMessage());
        }

        return false;

    }


}

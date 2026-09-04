package alexo.ecommerce_api.service.internal.jwt;

import alexo.ecommerce_api.dto.configuration.security.jwt.JwtPropertiesDTO;
import alexo.ecommerce_api.dto.service.internal.identity.UserPrincipalDTO;
import alexo.ecommerce_api.service.internal.identity.authority.UserPrincipalService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * Provides JWT token lifecycle operations for authentication.
 *
 * <p>The token includes user identity and authorization claims:
 * {@code userId}, {@code roles}, {@code permissions}.</p>
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtPropertiesDTO jwtPropertiesDTO;
    private final UserPrincipalService userPrincipalService;

    private SecretKey signingKey;

    /**
     * Initializes HMAC signing key from configured secret.
     *
     * @throws IllegalStateException when secret length is less than 32 bytes
     */
    @PostConstruct
    public void initializeKey() {
        byte[] keyBytes = jwtPropertiesDTO.secret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret must contain at least 32 bytes");
        }
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
         * Rebuilds authenticated principal from token claims without database lookup.
     *
     * @param token encoded JWT token
     * @return user principal
     */
    public UserPrincipalDTO getPrincipalFromToken(String token) {
        return (UserPrincipalDTO) userPrincipalService.loadUserById(Long.parseLong(extractAllClaims(token).getSubject()));
    }

    /**
     * Generates signed JWT access token for authenticated user.
     *
     * @param principal authenticated user principal
     * @return signed JWT token string
     */
    public String generateToken(UserPrincipalDTO principal) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(jwtPropertiesDTO.expirationMs());

        return Jwts.builder()
                .subject(principal.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Validates token structure and expiration for stateless authentication.
     *
     * @param token signed JWT token
     * @return {@code true} when token is parseable, not expired, and has required claims
     */
    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(extractAllClaims(token));
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * @return configured token TTL in milliseconds
     */
    public long getExpirationMs() {
        return jwtPropertiesDTO.expirationMs();
    }

    /**
     * Checks whether token expiration time is in the past.
     *
     * @param claims claims of JWT token
     * @return {@code true} if token is expired
     */
    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration == null || expiration.before(new Date());
    }

    /**
     * Parses signed token and returns full claim payload.
     *
     * @param token signed JWT token
     * @return JWT claims payload
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }

}

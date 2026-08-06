package alexo.ecommerce_api.configuration.security.jwt;

import alexo.ecommerce_api.entity.converter.EnumCodeMapper;
import alexo.ecommerce_api.entity.enums.PermissionCode;
import alexo.ecommerce_api.entity.enums.RoleCode;
import alexo.ecommerce_api.service.identity.auth.login.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Provides JWT token lifecycle operations for authentication.
 *
 * <p>The token includes user identity and authorization claims:
 * {@code userId}, {@code roles}, {@code permissions}.</p>
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_PERMISSIONS = "permissions";
    private static final String CLAIM_IS_ENABLED = "isEnabled";

    private final JwtProperties jwtProperties;

    private SecretKey signingKey;

    /**
     * Initializes HMAC signing key from configured secret.
     *
     * @throws IllegalStateException when secret length is less than 32 bytes
     */
    @PostConstruct
    void initializeKey() {
        byte[] keyBytes = jwtProperties.secret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret must contain at least 32 bytes");
        }
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates signed JWT access token for authenticated user.
     *
     * @param principal authenticated user principal
     * @return signed JWT token string
     */
    public String generateToken(UserPrincipal principal) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(jwtProperties.expirationMs());

        return Jwts.builder()
                .subject(principal.getUsername())
                .claim(CLAIM_USER_ID, principal.getId())
                .claim(CLAIM_ROLES, principal.getRoles())
                .claim(CLAIM_PERMISSIONS, principal.getPermissions())
                .claim(CLAIM_IS_ENABLED, principal.isEnabled())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extracts user identifier from token claim {@code userId}.
     *
     * @param token signed JWT token
     * @return user id from token
     * @throws IllegalArgumentException when claim is missing or has unsupported format
     */
    public Long extractUserId(String token) {
        Object rawUserId = extractAllClaims(token).get(CLAIM_USER_ID);
        if (rawUserId instanceof Number number) {
            return number.longValue();
        }
        if (rawUserId instanceof String value) {
            return Long.parseLong(value);
        }
        throw new IllegalArgumentException("Token does not contain a valid userId claim");
    }

    /**
     * Extracts user isEnabled flag from token claim {@code userId}.
     *
     * @param token signed JWT token
     * @return user id from token
     * @throws IllegalArgumentException when claim is missing or has unsupported format
     */
    public boolean extractIsEnabled(String token) {
        Object rawEnabled = extractAllClaims(token).get(CLAIM_IS_ENABLED);
        if (rawEnabled instanceof Boolean bool) {
            return bool;
        }
        if (rawEnabled instanceof String value) {
            return Boolean.parseBoolean(value);
        }
        throw new IllegalArgumentException("Token does not contain a valid isEnabled claim");
    }

    /**
     * Extracts role codes from token claim {@code roles}.
     *
     * @param token signed JWT token
     * @return immutable list of role codes
     */
    public List<RoleCode> extractRoles(String token) {
        return extractStringListClaim(token, CLAIM_ROLES)
                .stream()
                .map((String permissionCode) -> EnumCodeMapper.fromCode(RoleCode.class, permissionCode))
                .toList();
    }

    /**
     * Extracts permission codes from token claim {@code permissions}.
     *
     * @param token signed JWT token
     * @return immutable list of permission codes
     */
    public List<PermissionCode> extractPermissions(String token) {
        return extractStringListClaim(token, CLAIM_PERMISSIONS)
                .stream()
                .map((String permissionCode) -> EnumCodeMapper.fromCode(PermissionCode.class, permissionCode))
                .toList();
    }

    /**
     * Validates token ownership and expiration for provided principal.
     *
     * @param token signed JWT token
     * @param principal authenticated principal
     * @return {@code true} when token belongs to principal and is not expired
     */
    public boolean isTokenValid(String token, UserPrincipal principal) {
        Long tokenUserId = extractUserId(token);
        return tokenUserId.equals(principal.getId()) && !isTokenExpired(token);
    }

    /**
     * @return configured token TTL in milliseconds
     */
    public long getExpirationMs() {
        return jwtProperties.expirationMs();
    }

    /**
     * Checks whether token expiration time is in the past.
     *
     * @param token signed JWT token
     * @return {@code true} if token is expired
     */
    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Parses signed token and returns full claim payload.
     *
     * @param token signed JWT token
     * @return JWT claims payload
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts claim value and normalizes it to immutable list of strings.
     *
     * @param token signed JWT token
     * @param claimName claim key to extract
     * @return immutable list of string values, empty when claim is absent
     * @throws IllegalArgumentException when claim exists but is not a list
     */
    private List<String> extractStringListClaim(String token, String claimName) {
        Object rawClaim = extractAllClaims(token).get(claimName);

        if (rawClaim == null) {
            return Collections.emptyList();
        }

        if (!(rawClaim instanceof List<?> rawList)) {
            throw new IllegalArgumentException("Token claim " + claimName + " is not a list");
        }

        List<String> result = new ArrayList<>(rawList.size());
        for (Object value : rawList) {
            if (value == null) {
                continue;
            }
            result.add(value.toString());
        }

        return List.copyOf(result);
    }
}

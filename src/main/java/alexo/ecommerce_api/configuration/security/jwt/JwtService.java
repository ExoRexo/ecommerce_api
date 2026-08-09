package alexo.ecommerce_api.configuration.security.jwt;

import alexo.ecommerce_api.mapper.enums.EnumCodeMapper;
import alexo.ecommerce_api.enums.entity.PermissionCode;
import alexo.ecommerce_api.enums.entity.RoleCode;
import alexo.ecommerce_api.dto.service.identity.UserPrincipalDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
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

    private final JwtPropertiesDTO jwtPropertiesDTO;

    private SecretKey signingKey;

    /**
     * Initializes HMAC signing key from configured secret.
     *
     * @throws IllegalStateException when secret length is less than 32 bytes
     */
    @PostConstruct
    void initializeKey() {
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
        List<RoleCode> roles = extractRoles(token);
        List<PermissionCode> permissions = extractPermissions(token);

        return new UserPrincipalDTO(
                extractUserId(token),
                extractUsername(token),
            null,
                extractIsEnabled(token),
                roles,
                permissions,
                buildAuthorities(roles, permissions)
        );
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
                .subject(principal.getUsername())
                .claim(CLAIM_USER_ID, principal.getId())
                .claim(CLAIM_ROLES, principal.getRoles().stream().map(RoleCode::getCode).toList())
                .claim(CLAIM_PERMISSIONS, principal.getPermissions().stream().map(PermissionCode::getCode).toList())
                .claim(CLAIM_IS_ENABLED, principal.isEnabled())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extracts username from token claim {@code roles}.
     *
     * @param token signed JWT token
     * @return username
     */
    private String extractUsername(String token) {
        Object rawUsername = extractAllClaims(token).getSubject();
        if (rawUsername instanceof String value) {
            return value;
        }

        throw new IllegalArgumentException("Token does not contain a valid subject claim");
    }

    /**
     * Extracts user identifier from token claim {@code userId}.
     *
     * @param token signed JWT token
     * @return user id from token
     * @throws IllegalArgumentException when claim is missing or has unsupported format
     */
    private Long extractUserId(String token) {
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
    private boolean extractIsEnabled(String token) {
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
    private List<RoleCode> extractRoles(String token) {
        return extractStringListClaim(token, CLAIM_ROLES).stream().map((String permissionCode) -> EnumCodeMapper.fromCode(RoleCode.class, permissionCode)).toList();
    }

    /**
     * Extracts permission codes from token claim {@code permissions}.
     *
     * @param token signed JWT token
     * @return immutable list of permission codes
     */
    private List<PermissionCode> extractPermissions(String token) {
        return extractStringListClaim(token, CLAIM_PERMISSIONS).stream().map((String permissionCode) -> EnumCodeMapper.fromCode(PermissionCode.class, permissionCode)).toList();
    }

    /**
     * Validates token structure and expiration for stateless authentication.
     *
     * @param token signed JWT token
     * @return {@code true} when token is parseable, not expired, and has required claims
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return hasRequiredClaims(claims) && !isTokenExpired(claims);
        } catch (IllegalArgumentException ex) {
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

    /**
     * Builds Spring Security authorities from role and permission codes stored in token.
     *
     * @param roles role codes from token
     * @param permissions permission codes from token
     * @return immutable authority collection
     */
    private List<? extends GrantedAuthority> buildAuthorities(List<RoleCode> roles, List<PermissionCode> permissions) {
        LinkedHashSet<GrantedAuthority> authorities = new LinkedHashSet<>();

        roles.stream()
                .map((role) -> UserPrincipalDTO.ROLE_GRANTED_AUTHORITY_PREFIX + role.getCode())
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        permissions.stream()
                .map((permission) -> UserPrincipalDTO.PERMISSION_GRANTED_AUTHORITY_PREFIX + permission.getCode())
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        return List.copyOf(authorities);
    }

    /**
     * Checks minimal required claims for secure principal reconstruction.
     *
     * @param claims parsed token claims
     * @return {@code true} when token includes required identity claims
     */
    private boolean hasRequiredClaims(Claims claims) {
        String subject = claims.getSubject();
        if (subject == null || subject.isBlank()) {
            return false;
        }

        Object rawUserId = claims.get(CLAIM_USER_ID);
        if (rawUserId == null) {
            return false;
        }

        Object rawEnabled = claims.get(CLAIM_IS_ENABLED);
        return rawEnabled instanceof Boolean || rawEnabled instanceof String;
    }

    /**
     * Extracts claim value and normalizes it to immutable list of strings.
     *
     * @param token     signed JWT token
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

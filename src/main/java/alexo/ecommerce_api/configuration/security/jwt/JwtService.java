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

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_PERMISSIONS = "permissions";

    private final JwtProperties jwtProperties;

    private SecretKey signingKey;

    @PostConstruct
    void initializeKey() {
        byte[] keyBytes = jwtProperties.secret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret must contain at least 32 bytes");
        }
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserPrincipal principal) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(jwtProperties.expirationMs());

        return Jwts.builder()
                .subject(principal.getUsername())
                .claim(CLAIM_USER_ID, principal.getId())
                .claim(CLAIM_ROLES, principal.getRoles())
                .claim(CLAIM_PERMISSIONS, principal.getPermissions())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

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

    public List<RoleCode> extractRoles(String token) {
        return extractStringListClaim(token, CLAIM_ROLES)
                .stream()
                .map((String permissionCode) -> EnumCodeMapper.fromCode(RoleCode.class, permissionCode))
                .toList();
    }

    public List<PermissionCode> extractPermissions(String token) {
        return extractStringListClaim(token, CLAIM_PERMISSIONS)
                .stream()
                .map((String permissionCode) -> EnumCodeMapper.fromCode(PermissionCode.class, permissionCode))
                .toList();
    }

    public boolean isTokenValid(String token, UserPrincipal principal) {
        Long tokenUserId = extractUserId(token);
        return tokenUserId.equals(principal.getId()) && !isTokenExpired(token);
    }

    public long getExpirationMs() {
        return jwtProperties.expirationMs();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

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

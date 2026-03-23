package com.resortmanagement.system.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String secretKey;

    @Value("${application.security.jwt.expiration:86400000}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Return roles from the JWT as a list of strings.
     * Handles both:
     *  - "roles": "ADMIN"
     *  - "roles": ["ADMIN","EMPLOYEE"]
     *  - older "role": "ADMIN"
     */
    // @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            Object rolesObj = claims.get("roles");

            // backward-compat: single "role" key
            if (rolesObj == null) {
                Object single = claims.get("role");
                if (single instanceof String) {
                    return List.of(((String) single));
                }
                return Collections.emptyList();
            }

            // If it's a String -> single role
            if (rolesObj instanceof String) {
                return List.of((String) rolesObj);
            }

            // If it's a collection/array -> convert to List<String>
            if (rolesObj instanceof List) {
                List<?> raw = (List<?>) rolesObj;
                List<String> roles = new ArrayList<>();
                for (Object o : raw) {
                    if (o != null) roles.add(Objects.toString(o));
                }
                return roles;
            }

            // Fallback
            return Collections.emptyList();

        } catch (ExpiredJwtException | MalformedJwtException | SignatureException ex) {
            // treat invalid/expired token as no roles (higher-level filter already handles expiry)
            return Collections.emptyList();
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // FIX: Embed roles into the JWT so the token is self-contained and inspectable at jwt.io
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put("roles", roles);

        return buildToken(claims, userDetails, jwtExpiration);
    }

    


    // Uses jjwt 0.11.x API (setClaims, setSubject, setIssuedAt, setExpiration, signWith)
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // FIX: Wrapped in try/catch — a malformed or expired token was causing unhandled exceptions → 500
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Uses jjwt 0.11.x API (parserBuilder, setSigningKey, parseClaimsJws)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ✅ Add this new method — generates token with employeeId claim
    public String generateTokenWithEmployee(UserDetails userDetails, UUID employeeId) {
    Map<String, Object> extraClaims = new HashMap<>();
    if (employeeId != null) {
        extraClaims.put("employeeId", employeeId.toString());
    }
    return generateToken(extraClaims, userDetails);
    }

    // Add this method
public String generateTokenWithEmployeeAndPermissions(
        UserDetails userDetails, UUID employeeId, List<String> permissions) {
    Map<String, Object> extraClaims = new HashMap<>();
    if (employeeId != null) {
        extraClaims.put("employeeId", employeeId.toString());
    }
    if (permissions != null && !permissions.isEmpty()) {
        extraClaims.put("permissions", permissions); // ✅ embed as list
    }
    return generateToken(extraClaims, userDetails);
    }

    public List<String> extractPermissions(String token) {
    try {
        Claims claims = extractAllClaims(token);
        Object permsObj = claims.get("permissions");
        if (permsObj instanceof List) {
            List<?> raw = (List<?>) permsObj;
            List<String> perms = new ArrayList<>();
            for (Object o : raw) {
                if (o != null) perms.add(Objects.toString(o));
            }
            return perms;
        }
        return Collections.emptyList();
    } catch (ExpiredJwtException | MalformedJwtException | SignatureException ex) {
        return Collections.emptyList();
    }
}
}
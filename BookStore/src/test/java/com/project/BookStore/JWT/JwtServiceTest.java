package com.project.BookStore.JWT;

import com.project.BookStore.exception.InvalidJwtTokenSignatureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private jwtservice jwtService;

    @Mock
    private UserDetails userDetails;

    private String username = "testUser";
    private String token;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        KeyGenerator keyG = KeyGenerator.getInstance("HmacSHA256");
        SecretKey Skey = keyG.generateKey();
        String key = Base64.getEncoder().encodeToString(Skey.getEncoded());
        jwtService = new jwtservice();
        jwtService.key = key;

        Map<String, Object> claims = new HashMap<>();
        token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(jwtService.keyGenerator())
                .compact();
    }

    @Test
    void testGenerateToken() {
        String generatedToken = jwtService.generateToken(username);
        assertNotNull(generatedToken);
    }

    @Test
    void testExtractUsername() {
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateToken() {
        when(userDetails.getUsername()).thenReturn(username);
        boolean isValid = jwtService.validate(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void testExtractClaim() {
        Function<Claims, String> claimResolver = Claims::getSubject;
        String extractedClaim = jwtService.extractClaim(token, claimResolver);
        assertEquals(username, extractedClaim);
    }

    @Test
    void testIsTokenExpired() {
        boolean isExpired = jwtService.isTokenExpired(token);
        assertFalse(isExpired);
    }

    @Test
    void testExtractExpiration() {
        Date expirationDate = jwtService.extractExpiration(token);
        assertNotNull(expirationDate);
    }

    @Test
    void testInvalidJwtTokenSignatureException() {
        String invalidToken = token + "invalid";
        assertThrows(InvalidJwtTokenSignatureException.class, () -> {
            jwtService.extractsAllClaims(invalidToken);
        });
    }
}


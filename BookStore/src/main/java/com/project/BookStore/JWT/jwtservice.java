package com.project.BookStore.JWT;

import com.project.BookStore.exception.InvalidJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class jwtservice {

    protected String key;

    public jwtservice() throws NoSuchAlgorithmException {
        KeyGenerator keyG = KeyGenerator.getInstance("HmacSHA256");
        SecretKey Skey = keyG.generateKey();
        key = Base64.getEncoder().encodeToString(Skey.getEncoded());
    }

    public String generateToken(String username){
        Map<String,Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60))
                .and()
                .signWith(keyGenerator())
                .compact();

    }

    public SecretKey keyGenerator(){
        byte[] keyA = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyA);
    }

    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public boolean validate(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    Claims extractsAllClaims(String Token){
        try {
            return Jwts.parser()
                    .verifyWith(keyGenerator())
                    .build()
                    .parseSignedClaims(Token)
                    .getPayload();
        }
        catch (SignatureException exception){
            throw new InvalidJwtTokenException("Invalid JWT Token");
        }
    }

    <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractsAllClaims(token);
        return claimResolver.apply(claims);
    }

    boolean isTokenExpired(String token) {
        boolean isexpired = extractExpiration(token).before(new Date());
        return isexpired;
    }

    Date extractExpiration(String token) {
        Date date = extractClaim(token, Claims::getExpiration);
        return date;
    }
}

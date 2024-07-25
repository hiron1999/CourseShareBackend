package com.courseshare.AutorizationService.Utills;

import java.security.Key;

import java.util.*;
import java.util.function.Function;


import com.courseshare.AutorizationService.config.CustomUserDetails;
import com.courseshare.AutorizationService.reposetory.UserCredentialReposetory;
import com.courseshare.AutorizationService.service.CutomUserDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtill {

    public static final String USER_ID = "user_id";
    private Set<String> INVALID_TOKENS = new HashSet<>();
    @Autowired
    private UserCredentialReposetory credentialReposetory;
    @Autowired
    private CutomUserDetailsService userDetailsService;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public Long extractUserid(String refreshToken){
        return extractClaim(refreshToken,claims -> {
             Integer userid= (Integer) claims.get(USER_ID);
             return userid.longValue();
        });
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        final String email = extractUsername(token);
        UserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(email);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token) && !INVALID_TOKENS.contains(token) ;
    }
    //make access tokens invalid
    public void invalidateToken(String token){
        INVALID_TOKENS.add(token);
    }

    public Boolean validateRefreshToken(String token){
        final long id=extractUserid(token);
        Boolean isValid = true;

         boolean isActive="active".equals(credentialReposetory.findById(id).orElseThrow().getRefreshToken().getStatus());

         if(isTokenExpired(token)|| !isActive){
             isValid=false;
         }
//                ifPresent(userCredential -> {
//            final boolean isActive = "active".equals(userCredential.getRefreshToken().getStatus());
//            if(isTokenExpired(token)|| !isActive){
//                isValid[0] =false;
//            }
//        });


        return isValid;
    }
    public String generateToken(String email){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,email);
    }

    public String generateRefreshToken(long id){
        Map<String,Object> claims=new HashMap<>();
        claims.put(USER_ID,id);
        return createRefreshToken(claims);
    }

    private String createRefreshToken(Map<String, Object> claims) {
         return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*9))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        String SECRET = "c8ef0adf7d3a3d1c6130dde0e7322c3e0867c638cb9f9a949278e96b3a00a969";
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

package com.techproed.SpringBootJWT_Study;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {// bir depo gibi sik kullanilan method'larin saklandigi class'tir. !! Interview !!

    private String SECRET_KEY = "javacanlaraSelamOlsunBolcanaOfferHaberiAlmakIstiyoruz";//username ve pass encode edilerek secret edecek data

    private Claims extractAllClaims(String token) {//tokem parametreyi Claims data type dönüştüryor
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {//token ve verecegim Claims class'tan function
        final Claims claims = extractAllClaims(token);//yukarıdaki methodda Claims dats type dönüşen token
        return claimsResolver.apply(claims);
    }

    //Username token'den cekme (token match olamalı)
    public String extractUsername(String token) {//token parametre alıp token'daki  username return ediyor
        return extractClaim(token, Claims::getSubject);// Claims::getSubject-->method token parametrden username alıyor
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);//token'in ne zamam expire olacagını return eder.Son kullanma
    }

    private Boolean isTokenExpired(String token) {//token son kullanma tarihini gecip geçmediğini kontrol eder
        return extractExpiration(token).before(new Date());
    }

    private String createToken(Map<String, Object> claims, String subject) {//token cretae eden method
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)//password set eder
                .setIssuedAt(new Date(System.currentTimeMillis()))//ne zaman token create edildi set eder
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))//token son kullanma tarihi set eder 10 saat
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)//secret ve algorithm ile encode token set eder
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());//username tokene set edildi.
    }

    public Boolean validateToken(String token, UserDetails userDetails) {//App create ettiği token ile client'den gelen token  oney controlu
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}

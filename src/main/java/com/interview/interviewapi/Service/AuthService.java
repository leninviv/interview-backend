package com.interview.interviewapi.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    public String getJWTToken(String username){
        //clave secreta para generacion  de jwt
        String secretKey = "interviewSecretKey";

        Map<String, Object> claims = new HashMap<>();

        //generacion de jwt
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 6000000))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        return token;
    }

    public String validateJWT(String token){
        //validador de jwt sitodo bien devuelve subject donde esta el username logeuado
        Claims claims = Jwts.parser()
                .setSigningKey("interviewSecretKey")
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}

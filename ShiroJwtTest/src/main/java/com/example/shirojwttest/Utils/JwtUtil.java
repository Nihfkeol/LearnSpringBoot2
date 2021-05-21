package com.example.shirojwttest.Utils;

import com.example.shirojwttest.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;

public class JwtUtil {

    private final static String sign = "asd84cm,./5";

    public static String getToken(User user) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        return Jwts.builder()
                .claim("username", user.getName())
                .claim("password", user.getPassword())
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS256, sign)
                .compact();

    }

    public static Claims verify(String token){
        return (Claims) Jwts.parser().setSigningKey(sign).parse(token).getBody();
    }
}

package com.webflux.springwebflux.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")//toma la informacion de properties
    private String secret;
    @Value("${jwt.expiration}")
    private int expiration;

    //generamos token
    public String generateToken(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(getKey(secret))
                .compact();
    }

    //claim piezas de info de un usuario que se encuentran firmadas en un token
    public Claims getClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //obtenemos el nombre de usuario
    public String getSubject(String token){
        return Jwts.parserBuilder().setSigningKey(getKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //si el token esta mal formado o expirado lanza una excepcion
    public boolean validate(String token){
        try {
            Jwts.parserBuilder().setSigningKey(getKey(secret))
                    .build()
                    .parseClaimsJws(token)
                    .getBody(); //si no lanza una excepcion el token esta bien
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired");
        } catch (UnsupportedJwtException e) {
            log.error("Token unsupported");
        } catch (MalformedJwtException e) {
            log.error("Token malformed");
        } catch (SignatureException e) {
            log.error("Bad signature");
        } catch (IllegalArgumentException e) {
            log.error("Illegal args");
        }
        return false;
    }

    //ciframos el password
    private Key getKey(String secret){
        //convertimos en array de bytes
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }
}

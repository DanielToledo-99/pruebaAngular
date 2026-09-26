package com.inventario.producto.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class UtilidadesJwt {

    private final Key claveFirma;
    private final long expiracionMs;

    public UtilidadesJwt(@Value("${jwt.secreto}") String secreto, @Value("${jwt.expiracion-ms}") long expiracionMs) {
        this.claveFirma = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = expiracionMs;
    }

    public String generarToken(String usuario) {
        Date ahora = new Date();
        Date vencimiento = new Date(ahora.getTime() + expiracionMs);
        return Jwts.builder()
                .setSubject(usuario)
                .setIssuedAt(ahora)
                .setExpiration(vencimiento)
                .signWith(claveFirma, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraerUsuario(String token) {
        return interpretarAtributos(token).getSubject();
    }

    public boolean esTokenValido(String token) {
        try {
            Claims atributos = interpretarAtributos(token);
            return atributos.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private Claims interpretarAtributos(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(claveFirma)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

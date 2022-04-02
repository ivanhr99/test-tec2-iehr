package com.test.testtec2.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtility {

    private final static Logger logger = LoggerFactory.getLogger(JwtUtility.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.issuer}")
    private String issuer;


    public String getUserNameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token,Claims::getExpiration);
    }

    public<T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }


    private Boolean isTokenExpired(String token){
        final Date expiration =  getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }
    public String generateTokenUserName(String username){
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", "User");
        return doGenerateToken(claims, username);
    }
    private String doGenerateToken(Map<String, Object> claims, String subject){

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        String id ="123";
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(this.secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());


        logger.error("se genero : " + id + ", " +  now + "," + nowMillis) ;


        JwtBuilder builder = Jwts.builder()
        .setSubject(subject)				
        .signWith(signatureAlgorithm, signingKey);

        logger.error("se genero : " + builder);

        if (this.expiration >= 0) {
            long expMillis = nowMillis + this.expiration;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();

    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username =  getUserNameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean validateTokenEstructure(String token){
        try{
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        }catch(MalformedJwtException e){
            logger.error("Token Mal Formado " + token, e);
        }catch(UnsupportedJwtException e){
            logger.error("Token No Soportado " + token, e);
        }catch(ExpiredJwtException e){
            logger.error("Token Expirado " + token, e);
        }catch(IllegalArgumentException e){
            logger.error("Token Vacio " + token, e);
        }catch(SignatureException e){
            logger.error("Token No Firmado " + token, e);
        }

        return false;
    }

}

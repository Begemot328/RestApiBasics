package com.epam.esm.web.security.jwt;

import com.epam.esm.persistence.model.userdetails.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import java.sql.Date;
import java.time.LocalDate;

/**
 * JWT token processing util class.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Component
@PropertySource("classpath:jwtutil.properties")
public class JwtTokenUtil {

    @Value("${jwtSecret}")
    private String jwtSecret;
    @Value("${jwtIssuer}")
    private String jwtIssuer;

    /**
     * Generate {@link String} JWT token for {@link Account}.
     *
     * @param user User's {@link Account} to geterate token for.
     * @return {@link String} JWT token.
     */
    public String generateAccessToken(Account user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuer(jwtIssuer)
                .setIssuedAt(Date.valueOf(LocalDate.now()))
                .setExpiration(Date.valueOf(LocalDate.now().plusDays(7)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Get username from {@link String} JWT token for {@link Account}.
     *
     * @param token User's JWT token .
     * @return {@link String} username value.
     */
    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Get expiration data from {@link String} JWT token.
     *
     * @param token User's JWT token .
     * @return {@link Date} value.
     */
    public java.util.Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    /**
     * Validate {@link String} JWT token.
     *
     * @param token User's JWT token .
     * @return  true if token is valid.
     */
    public boolean validate(String token) throws AuthenticationException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException
                | ExpiredJwtException | UnsupportedJwtException
                | IllegalArgumentException ex) {
            throw new AuthenticationException(ex.getClass().getName() + " " + ex.getMessage());
        }
    }
}

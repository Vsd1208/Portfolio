package com.taskmanager.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
@Service
public class JwtService {
  private final SecretKey key; private final long accessMinutes;
  public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.access-minutes:15}") long minutes){
    key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); accessMinutes=minutes;
  }
  public String access(String email){return token(email,accessMinutes,"access");}
  public String refresh(String email){return token(email,60*24*7,"refresh");}
  private String token(String email,long minutes,String type){Instant now=Instant.now(); return Jwts.builder().subject(email).claim("type",type)
    .issuedAt(Date.from(now)).expiration(Date.from(now.plus(minutes,ChronoUnit.MINUTES))).signWith(key).compact();}
  public String email(String token,String type){Claims c=Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    if(!type.equals(c.get("type",String.class))) throw new JwtException("Wrong token type"); return c.getSubject();}
}

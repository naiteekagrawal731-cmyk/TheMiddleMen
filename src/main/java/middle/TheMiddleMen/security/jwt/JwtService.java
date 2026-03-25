package middle.TheMiddleMen.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private Long expiryTimeInMili = 1000*60*10l;

    public Key getSigingKey(){
        byte[] secretKeyInByte = secretKey.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(secretKeyInByte);
    }

    public String generateAccessToken(UserDetails userDetails){
        HashMap<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .signWith(getSigingKey(), SignatureAlgorithm.HS256)
                .setIssuedAt(new Date())
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis()+expiryTimeInMili))
                .compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(String token){
        try {
            return extractAllClaims(token).getSubject();
        }catch (JwtException e){
            return null;
        }
    }

    public boolean isTokenExpired(String token){
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        }catch (JwtException e){

            return true;
        }
    }

    public boolean isTokenValid(String token,UserDetails userDetails){
        if(userDetails.getUsername().equals(getUsername(token)) && !isTokenExpired(token)){
            return true;
        }
        return false;
    }



}

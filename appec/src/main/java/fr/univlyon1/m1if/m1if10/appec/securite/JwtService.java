package fr.univlyon1.m1if.m1if10.appec.securite;

import fr.univlyon1.m1if.m1if10.appec.dao.JpaJwtDao;
import fr.univlyon1.m1if.m1if10.appec.model.Jwt;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY ="V90Y519EQB2B1BAC8WYFQ6ECOREL1CMO56IW29385CPP3F4152QFMFPAFVTJIABAA7UG2ZAAOMPYP6F9KK34B9136AQGD7VT8I9X5OLBWPRDTQJXYKCW0TT74CRHSHQC5AM2Q4174C9KMY69OMWOV8R5WYRLO8KPV29L5W3825Q51HUJ8FK88XA39BRFOBUVLP0WEA5YSXM3SLMW09M40PQKIXDFO95W8YB0OESZ1YMIGWSAQ3EWH49VB1ST0X64";

    @Autowired
    private JpaJwtDao jpajwtDao;


    public String extractUserLogin(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
        String token = generateToken(new HashMap<>(), user);
        final Jwt jwt = new Jwt();
        jwt.setToken(token);
        jwt.setUser(user);
        jpajwtDao.save(jwt);
        return token;
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        try {
            final String userlogin = extractUserLogin(jwt);
            return (userlogin.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
        } catch (Exception e) {
            return false;
        }

    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Jwt tokenByValue(String token) {
        Optional<Jwt> jwt = jpajwtDao.findByValue(token);
        return jwt.orElse(null);
    }

    public void desactiveToken(User user) {
        Optional<Jwt> jwt = jpajwtDao.findTokenValidByUser(user);
        jwt.ifPresent(value -> jpajwtDao.delete(jwt.get()));
    }
}

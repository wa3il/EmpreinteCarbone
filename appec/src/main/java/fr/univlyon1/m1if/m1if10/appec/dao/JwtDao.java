package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.Jwt;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JwtDao extends JpaJwtDao {

    /**
     * Find a jwt by token.
     *
     * @param token the token of the jwt
     * @return the jwt if found, null otherwise
     */
    public Optional<Jwt> findByValue(String token) {
        for (Jwt jwt : getAll()) {
            if (jwt.getToken().equals(token)) {
                return Optional.of(jwt);
            }
        }
        return Optional.empty();
    }

    /**
     * Find a jwt by user.
     *
     * @param user the user of the jwt
     * @return the jwt if found, null otherwise
     */
    public Optional<Jwt> findTokenValidByUser(User user) {
        for (Jwt jwt : getAll()) {
            if (jwt.getUser().getUsername().equals(user.getUsername()) && !jwt.isExpire() && !jwt.isDesactive()) {
                return Optional.of(jwt);
            }
        }
        return Optional.empty();
    }
}
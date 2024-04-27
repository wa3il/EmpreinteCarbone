package fr.univlyon1.m1if.m1if10.appec.dao;

import fr.univlyon1.m1if.m1if10.appec.model.User;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao extends JpaUserDao{

    /**
     * Find a user by email.
     * @param login the email of the user
     * @return the user if found, null otherwise
     */
    public Optional<User> findByLogin(@NotNull String login) {
        for(User user : getAll()){
            if(user.getUsername().equals(login)){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}

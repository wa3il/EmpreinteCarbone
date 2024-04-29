package fr.univlyon1.m1if.m1if10.appec.controller;

import fr.univlyon1.m1if.m1if10.appec.dao.JpaJwtDao;
import fr.univlyon1.m1if.m1if10.appec.dao.JpaUserDao;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AuthenticationResponse;
import fr.univlyon1.m1if.m1if10.appec.dto.user.UserRequestDto;
import fr.univlyon1.m1if.m1if10.appec.model.Jwt;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import fr.univlyon1.m1if.m1if10.appec.securite.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final JpaUserDao jpaUserDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final JpaJwtDao JpajwtDao;

    @Autowired
    public AuthenticationService(JpaUserDao jpaUserDao, PasswordEncoder passwordEncoder, JwtService jwtService,
                                 AuthenticationManager authenticationManager, @Qualifier("jpaJwtDao") JpaJwtDao jpajwtDao) {
        this.jpaUserDao = jpaUserDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.JpajwtDao = jpajwtDao;
    }

    public AuthenticationResponse register(UserRequestDto userRequestDto) {
        User user = new User(userRequestDto.getName(), passwordEncoder.encode(userRequestDto.getPassword()),
                userRequestDto.getLogin());
        jpaUserDao.save(user);
        return new AuthenticationResponse(jwtService.generateToken(user));
    }

    public AuthenticationResponse authenticate(UserRequestDto userRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequestDto.getLogin(),
                        userRequestDto.getPassword()));
        Optional<User> user = jpaUserDao.findByLogin(userRequestDto.getLogin());

        if (user.isEmpty()) {throw new RuntimeException("User not found");}
        Optional<Jwt> jwt = JpajwtDao.findTokenValidByUser(user.get());
        if (jwt.isPresent() ) {
            if (!jwt.get().isDesactive() && jwtService.isTokenValid(jwt.get().getToken(), user.get())){
                return new AuthenticationResponse(jwt.get().getToken());
            } else {
                JpajwtDao.delete(jwt.get());
                return new AuthenticationResponse(jwtService.generateToken(user.get()));
            }
        } else {
            return new AuthenticationResponse(jwtService.generateToken(user.get()));
        }

    }

    public void deconnexion() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Jwt> jwt = JpajwtDao.findTokenValidByUser(user);
        if (jwt.isPresent()){
            JpajwtDao.delete(jwt.get());
        }
    }

    public String encoderPassword(String password) {
        return passwordEncoder.encode(password);
    }
}

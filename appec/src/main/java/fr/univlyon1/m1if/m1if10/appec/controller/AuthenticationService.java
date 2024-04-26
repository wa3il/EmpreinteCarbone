package fr.univlyon1.m1if.m1if10.appec.controller;

import fr.univlyon1.m1if.m1if10.appec.dao.JwtDao;
import fr.univlyon1.m1if.m1if10.appec.dao.UserDao;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AuthenticationResponse;
import fr.univlyon1.m1if.m1if10.appec.dto.user.UserRequestDto;
import fr.univlyon1.m1if.m1if10.appec.model.Jwt;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import fr.univlyon1.m1if.m1if10.appec.securite.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final JwtDao jwtDao;

    @Autowired
    public AuthenticationService(UserDao userDao, PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authenticationManager, JwtDao jwtDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.jwtDao = jwtDao;
    }

    public AuthenticationResponse register(UserRequestDto userRequestDto) {
        User user = new User(userRequestDto.getName(), passwordEncoder.encode(userRequestDto.getPassword()),
                userRequestDto.getLogin());
        userDao.save(user);
        return new AuthenticationResponse(jwtService.generateToken(user));
    }

        public AuthenticationResponse authenticate(UserRequestDto userRequestDto) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequestDto.getLogin(),
                            userRequestDto.getPassword()));
            Optional<User> user = userDao.findByLogin(userRequestDto.getLogin());

            if (user.isEmpty()) {
                throw new RuntimeException("User not found");

            } else if (user.isPresent()) {
                Optional<Jwt> jwt = jwtDao.findTokenValidByUser(user.get());
                /*if (jwt.isPresent() && (jwt.get().isExpire() == false) && (!jwt.get().isDesactive()) {
                    return new AuthenticationResponse(jwt.get().getToken());
                } else {
                    return new AuthenticationResponse(jwtService.generateToken(user.get()));
                }*/
                return new AuthenticationResponse(jwtService.generateToken(user.get()));
            } else {
                throw new RuntimeException("User not found");
            }

        }

    public void deconnexion() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.jwtService.desactiveToken(user);
    }
}

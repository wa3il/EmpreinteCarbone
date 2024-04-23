package fr.univlyon1.m1if.m1if10.appec.controller;

import fr.univlyon1.m1if.m1if10.appec.dao.UserDao;
import fr.univlyon1.m1if.m1if10.appec.dto.user.AuthenticationResponse;
import fr.univlyon1.m1if.m1if10.appec.dto.user.UserRequestDto;
import fr.univlyon1.m1if.m1if10.appec.model.User;
import fr.univlyon1.m1if.m1if10.appec.securite.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserDao userDao, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public AuthenticationResponse register(UserRequestDto userRequestDto) {
        User user = new User(userRequestDto.getName(), passwordEncoder.encode(userRequestDto.getPassword()), userRequestDto.getLogin());
        userDao.save(user);
        return new AuthenticationResponse(jwtService.generateToken(user));
    }

    public AuthenticationResponse authenticate(UserRequestDto userRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequestDto.getLogin(),
                        userRequestDto.getPassword()
                ));
        Optional<User> user = userDao.findByLogin(userRequestDto.getLogin());
        if(user.isEmpty()) {
            throw new RuntimeException("User not found");
        }else {
            return new AuthenticationResponse(jwtService.generateToken(user.get()));
        }
    }
}

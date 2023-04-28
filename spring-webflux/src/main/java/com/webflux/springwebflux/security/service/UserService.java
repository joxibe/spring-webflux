package com.webflux.springwebflux.security.service;

import com.webflux.springwebflux.exception.CustomException;
import com.webflux.springwebflux.security.dto.CreateUserDto;
import com.webflux.springwebflux.security.dto.LoginDto;
import com.webflux.springwebflux.security.dto.TokenDto;
import com.webflux.springwebflux.security.entity.User;
import com.webflux.springwebflux.security.enums.Role;
import com.webflux.springwebflux.security.jwt.JwtProvider;
import com.webflux.springwebflux.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public Mono<TokenDto> login(LoginDto loginDto){
        return userRepository.findByUsernameOrEmail(loginDto.getUsername(), loginDto.getUsername())
                .filter(user -> passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
                .map(user -> new TokenDto(jwtProvider.generateToken(user)))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "bad credentials")));
    }

    public  Mono<User> create(CreateUserDto createUserDto){
        User user = User.builder()
                .username(createUserDto.getUsername())
                .email(createUserDto.getEmail())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .roles(Role.ROLE_USER.name()) //quitamos Role.ROLE_ADMIN.name() + ", " +  para que el usuario que se cree solo tenga el rol de user
                .build();

        Mono<Boolean> userExists = userRepository
                .findByUsernameOrEmail(user.getUsername(), user.getEmail()).hasElement(); //porque es un booleano
        return userExists
                .flatMap(exists -> exists ?
                        Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "username or email already in use"))
                        : userRepository.save(user));
    }
}

package com.webflux.springwebflux.security.handler;

import com.webflux.springwebflux.security.dto.CreateUserDto;
import com.webflux.springwebflux.security.dto.LoginDto;
import com.webflux.springwebflux.security.dto.TokenDto;
import com.webflux.springwebflux.security.entity.User;
import com.webflux.springwebflux.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthHandler {

    private final UserService userService;

    public Mono<ServerResponse> login (ServerRequest request){
        Mono<LoginDto> dtoMono = request.bodyToMono(LoginDto.class);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userService.login(dto), TokenDto.class));
    }

    public Mono<ServerResponse> create (ServerRequest request) {
        Mono<CreateUserDto> dtoMono = request.bodyToMono(CreateUserDto.class);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userService.create(dto), User.class));
    }
}

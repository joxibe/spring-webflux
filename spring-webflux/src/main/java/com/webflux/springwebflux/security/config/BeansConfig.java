package com.webflux.springwebflux.security.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeansConfig {

    //se usa para cifrar el password de un usuario guardado y cuando haga login para comprobor
    //si el password que envia el usuario si cifrar es la que tiene guardada
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebProperties.Resources resources (){
        return new WebProperties.Resources();
    }
}

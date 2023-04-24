package com.webflux.springwebflux.router;

import com.webflux.springwebflux.handler.ProductHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class ProductRouter {

    private static final String PATH = "product";

    @Bean
    public WebProperties.Resources resources (){
        return new WebProperties.Resources();
    }

    @Bean
    RouterFunction<ServerResponse> router(ProductHandler productHandler){
        return RouterFunctions.route()
                .GET(PATH, productHandler::getAll) //obtener la lista
                .GET(PATH + "/{id}", productHandler::getOne)
                .POST(PATH, productHandler::save)
                .PUT(PATH + "/{id}", productHandler::update)
                .DELETE(PATH + "/{id}", productHandler::deleteById)
                .build();
    }
}

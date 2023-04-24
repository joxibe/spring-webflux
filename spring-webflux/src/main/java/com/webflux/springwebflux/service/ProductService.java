package com.webflux.springwebflux.service;

import com.webflux.springwebflux.dto.ProductDto;
import com.webflux.springwebflux.entity.Product;
import com.webflux.springwebflux.exception.CustomException;
import com.webflux.springwebflux.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor //para no colocar autowired
public class ProductService {

    private final static String NF_MESSAGE = "producto not found";
    private final static String NAME_MESSAGE = "Product name alreany in use";

    //final por la anotacion de requiredArgsContructor
    private final ProductRepository productRepository;

    public Flux<Product> getAll(){
        return productRepository.findAll();
    }

    public Mono<Product> getById(int id){
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE)));
    }

    public Mono<Product> save(ProductDto dto){
        //obetnemos un nombre, pero con has element obtenemos un si o un no
        Mono<Boolean> existName = productRepository.findByName(dto.getName()).hasElement();
        return existName.flatMap(exists -> exists
                ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST, NAME_MESSAGE))
                : productRepository.save(Product.builder().name(dto.getName()).price(dto.getPrice()).build()));
    }

    public Mono<Product> update(int id, ProductDto dto){
//        Product newProduct = new Product(id, product.getName(), product.getPrice());
//        return productRepository.save(newProduct);
        Mono<Boolean> productId = productRepository.findById(id).hasElement();
        Mono<Boolean> productRepeatedName = productRepository.repeatedName(id, dto.getName()).hasElement();
        return productId.flatMap(
                existsId -> existsId ?
                        productRepeatedName.flatMap(existsName -> existsName ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST, NAME_MESSAGE))
                                : productRepository.save(new Product(id, dto.getName(), dto.getPrice())))
                                : Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE)));
    }

    public Mono<Void> delete(int id){
        Mono<Boolean> productId = productRepository.findById(id).hasElement();
        return productId.flatMap(exists -> exists ? productRepository.deleteById(id) : Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE)));
    }
}

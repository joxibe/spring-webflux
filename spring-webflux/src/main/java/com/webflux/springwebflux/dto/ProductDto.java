package com.webflux.springwebflux.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDto {

    @NotBlank(message = "Name is mandatory")
    private String name;
    @Min(value = 1, message = "Price must be greater than zero")
    private float price;
}

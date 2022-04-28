package com.simpleproductapp.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProductException extends RuntimeException{

    public ProductException(String message) {
        super(message);
    }
}

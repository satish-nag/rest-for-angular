package com.test.spring.exceptions;

public class ClientException extends RuntimeException{
    public ClientException(Throwable cause) {
        super(cause);
    }
}

package com.project.BookStore.exception;

public class InvalidJwtTokenSignatureException extends RuntimeException{
    public InvalidJwtTokenSignatureException(String message){
        super(message);
    }
}

package com.example.springboot.common;

public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}

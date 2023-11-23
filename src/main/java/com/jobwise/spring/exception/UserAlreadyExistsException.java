package com.jobwise.spring.exception;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/13/2023
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super(String.format("User with email [%s] already exists", email));
    }
}

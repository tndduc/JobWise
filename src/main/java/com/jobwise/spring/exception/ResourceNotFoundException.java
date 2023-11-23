package com.jobwise.spring.exception;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/13/2023
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

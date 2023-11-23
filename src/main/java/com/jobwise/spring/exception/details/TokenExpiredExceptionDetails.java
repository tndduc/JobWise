package com.jobwise.spring.exception.details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/13/2023
 */
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenExpiredExceptionDetails extends ExceptionDetails {

    private boolean expired;

}
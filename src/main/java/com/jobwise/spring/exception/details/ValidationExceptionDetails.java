package com.jobwise.spring.exception.details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/13/2023
 */
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationExceptionDetails extends ExceptionDetails {

    private Map<String, List<String>> fieldErrors;

}
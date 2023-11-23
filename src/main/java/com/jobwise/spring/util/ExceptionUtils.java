package com.jobwise.spring.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/13/2023
 */
public class ExceptionUtils {

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(object);
    }

}
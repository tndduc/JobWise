package com.jobwise.spring.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/13/2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageResponse {
    public String type;

    public String message;

}

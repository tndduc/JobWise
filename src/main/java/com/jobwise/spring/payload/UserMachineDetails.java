package com.jobwise.spring.payload;

import lombok.*;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/11/2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserMachineDetails {

    private String browser;
    private String operatingSystem;
    private String ipAddress;

}

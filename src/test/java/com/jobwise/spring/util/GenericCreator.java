package com.jobwise.spring.util;

import com.jobwise.spring.payload.UserMachineDetails;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/21/2023
 */
public class GenericCreator {

    public static final String BROWSER = "browser-test";
    public static final String OPERATING_SYSTEM = "os-test";
    public static final String ID_ADDRESS = "ip-test";

    public static UserMachineDetails createUserMachineDetails() {
        return UserMachineDetails
                .builder()
                .operatingSystem(OPERATING_SYSTEM)
                .ipAddress(ID_ADDRESS)
                .browser(BROWSER)
                .build();
    }

}

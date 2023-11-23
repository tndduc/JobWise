package com.jobwise.spring.util.auth;

import com.jobwise.spring.security.service.UserDetailsImpl;
import com.jobwise.spring.util.user.UserCreator;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/21/2023
 */
public class UserDetailsImplCreator {

    public static UserDetailsImpl createUserDetails() {
        return UserDetailsImpl.build(UserCreator.createUser());
    }

}

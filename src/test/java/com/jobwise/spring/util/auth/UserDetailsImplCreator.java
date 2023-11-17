package com.jobwise.spring.util.auth;

import com.jobwise.spring.security.service.UserDetailsImpl;
import com.jobwise.spring.util.user.UserCreator;

public class UserDetailsImplCreator {

    public static UserDetailsImpl createUserDetails() {
        return UserDetailsImpl.build(UserCreator.createUser());
    }

}

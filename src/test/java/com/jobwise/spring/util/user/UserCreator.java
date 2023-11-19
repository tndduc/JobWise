package com.jobwise.spring.util.user;

import com.jobwise.spring.payload.request.PromoteRequest;
import com.jobwise.spring.model.ERole;
import com.jobwise.spring.model.Role;
import com.jobwise.spring.model.User;

import java.util.Set;
import java.util.UUID;

public class UserCreator {

    public static final UUID ID = UUID.fromString("4ba29b2f-1e85-44bc-bebf-d909b33ec16e");
    public static final String FIRST_NAME = "adam";
    public static final String LAST_NAME = "adam";

    public static final String EMAIL = "adam@gmail.com";
    public static final String PASSWORD = "123456";
    public static final Set<Role> ROLES = Set.of(new Role(ERole.ROLE_USER));

    public static User createUserToBeSave() {
        return User
                .builder()
                .first_name(FIRST_NAME)
                .last_name(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .roles(ROLES)
                .build();
    }

    public static User createUser() {
        return User
                .builder()
                .id(ID)
                .first_name(FIRST_NAME)
                .last_name(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .roles(ROLES)
                .build();
    }

    public static PromoteRequest createPromoteRequest() {
        return PromoteRequest
                .builder()
                .userId(ID.toString())
                .roles(Set.of("admin"))
                .build();


    }
}

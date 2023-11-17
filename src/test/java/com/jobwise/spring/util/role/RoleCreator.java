package com.jobwise.spring.util.role;

import com.jobwise.spring.model.ERole;
import com.jobwise.spring.model.Role;

import java.util.UUID;

public class RoleCreator {

    public static final UUID ID = UUID.fromString("357dc489-a26e-4959-ae07-33487d831193");
    public static final ERole NAME = ERole.ROLE_USER;


    public static Role createRoleToBeSave() {
        return Role
                .builder()
                .name(NAME)
                .build();
    }

    public static Role createRole() {
        return Role
                .builder()
                .id(ID)
                .name(NAME)
                .build();
    }

}

package com.jobwise.spring.util;

import com.jobwise.spring.model.ERole;

import java.util.HashMap;
import java.util.Map;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/13/2023
 */
public class RoleUtils {

    public static Map<String, ERole> roles = new HashMap<>() {{
        put("admin", ERole.ROLE_ADMIN);
        put("mod", ERole.ROLE_MODERATOR);
        put("user", ERole.ROLE_USER);
    }};

    public static ERole getRoleByString(String name) {
        return roles.getOrDefault(name.toLowerCase(), ERole.ROLE_USER);
    }


}

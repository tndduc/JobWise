package com.jobwise.spring.util;

import com.jobwise.spring.security.service.UserDetailsImpl;
import com.jobwise.spring.util.auth.UserDetailsImplCreator;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public class MockUtils {

    public static void mockSecurityContextHolder(boolean nullPrincipal) {
        Authentication auth = Mockito.mock(Authentication.class);

        UserDetailsImpl userDetails = nullPrincipal ? null : UserDetailsImplCreator.createUserDetails();

        Mockito
                .when(auth.getPrincipal())
                .thenReturn(userDetails);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        Mockito
                .when(securityContext.getAuthentication())
                .thenReturn(auth);

        SecurityContextHolder.setContext(securityContext);
    }

    public static void mockSecurityContextHolder() {
        mockSecurityContextHolder(false);
    }

    public static HttpServletRequest mockUserMachineInfo() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);

        Mockito
                .when(httpServletRequest.getHeader(""))
                .thenReturn("");

        return httpServletRequest;
    }

}

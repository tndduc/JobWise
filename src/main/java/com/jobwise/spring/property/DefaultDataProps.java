package com.jobwise.spring.property;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/11/2023
 */
@Configuration
@ConfigurationProperties(prefix = "app.init")
@Getter
@Setter
@ToString
public class DefaultDataProps {

    @NestedConfigurationProperty
    private DefaultUser user = new DefaultUser();

    @Getter
    @Setter
    @ToString
    public static class DefaultUser {

        private String email = "admin@mail.com";
        private String password = "admin123";
        private String first_name = "Admin";

        private String last_name = "Happ";
    }

}

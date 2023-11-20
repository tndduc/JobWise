package com.jobwise.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
public class User extends BaseEntity {

    @NotBlank
    @Size(max = 50)
    private String email;
    private Date create_at;

    private Date update_at;
    @NotBlank
    @Size(max = 50)
    private String first_name;
    @NotBlank
    @Size(max = 50)
    private String last_name;

    @Size(max = 50)
    private String photo;
    @Size(max = 50)
    private String address;
    @Size(max = 50)
    private String phone;
    @Size(max = 150)
    private String company_id;
    @Size(max = 150)
    private String school_id;
    @Size(max = 150)
    private String description;
    @Size(max = 50)
    private String status;

    @NotBlank
    @Size(max = 150)
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();



}

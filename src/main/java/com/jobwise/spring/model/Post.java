package com.jobwise.spring.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Setter
@Getter
@Entity(name = "post")
public class Post extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Schema(description = "Owner of post")
    private User user;
    @Column(nullable = false)
    private String caption;
    @Schema(description = "This is url of picture")
    private String photo;
    @Schema(description = "This is status of post")
    private String status;

}

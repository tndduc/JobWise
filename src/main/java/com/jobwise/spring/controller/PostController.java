package com.jobwise.spring.controller;

import com.jobwise.spring.model.Post;
import com.jobwise.spring.model.User;
import com.jobwise.spring.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/21/2023
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
final PostService postService;
    @GetMapping("/all")
    @Operation(summary = "Returns all posts with pagination", tags = "Posts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "403", description = "When forbidden"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<Page<Post>> allPost(Pageable pageable) {
        return ResponseEntity.ok(postService.listAll(pageable));
    }
    @RequestMapping(path = "/upload",
            method = RequestMethod.POST,consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a post", tags = "Posts")
    public ResponseEntity<?> addPost(@RequestParam(value = "title", required = true)String title,
            @RequestParam (value = "file", required = false) MultipartFile file
    ) {
        return postService.addPost(file, title);
    }

    @RequestMapping(path = "/update/{postId}",
            method = RequestMethod.PATCH,consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update post", tags = "Posts")
    public ResponseEntity<?> updatePost(
            @PathVariable("postId") @NotNull(message = "Post ID is required") UUID postId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("title") String title
    ) {
        ResponseEntity<?> response = postService.updatePost(postId, file, title);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
    @DeleteMapping("/{postId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete post", tags = "Posts")
    public ResponseEntity<?> deletePost(@PathVariable UUID postId) {
        return postService.deletePost(postId);
    }

}

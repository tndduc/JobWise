package com.jobwise.spring.service;

import com.jobwise.spring.model.Post;
import com.jobwise.spring.model.User;
import com.jobwise.spring.payload.response.MessageResponse;
import com.jobwise.spring.repository.PostRepository;
import com.jobwise.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/21/2023
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class PostService {
    final PostRepository postRepository;
    final UserRepository userRepository;
    final FileUploadImpl fileUpload;
    public Page<Post> listAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public ResponseEntity<?> addPost(MultipartFile file, String title) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            MessageResponse errorResponse = new MessageResponse("authentication","failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            MessageResponse errorResponse = new MessageResponse("authentication","failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        User user = userOptional.get();
        try {
            if (title == null || title.isEmpty()) {
                MessageResponse errorResponse = new MessageResponse("caption","empty");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            String imageURL = null;
            if (file != null && !file.isEmpty()) {
                imageURL = fileUpload.uploadFile(file);
            }
            String status = "active";
            Post post = Post.builder()
                    .photo(imageURL)
                    .user(user)
                    .caption(title)
                    .status(status)
                    .build();

            Post savedPost = postRepository.save(post);
            return ResponseEntity.ok(savedPost);
        } catch (IOException e) {
            e.printStackTrace();
            MessageResponse errorResponse = new MessageResponse("file","upload_failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    public ResponseEntity<?> updatePost(UUID postId, MultipartFile file, String title) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            MessageResponse errorResponse = new MessageResponse("authentication","failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            MessageResponse errorResponse = new MessageResponse("authentication","failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        User loggedInUser = userOptional.get();

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            MessageResponse errorResponse = new MessageResponse("post","not_found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Post post = optionalPost.get();

        if (!post.getUser().equals(loggedInUser)) {
            MessageResponse errorResponse = new MessageResponse("authorization","denied");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        try {
            if (title != null && !title.isEmpty()) {
                post.setCaption(title);
            }

            if (file != null && !file.isEmpty()) {
                String imageURL = fileUpload.uploadFile(file);
                post.setPhoto(imageURL);
            }

            post.setCaption(title);

            Post updatedPost = postRepository.save(post);
            return ResponseEntity.ok(updatedPost);
        } catch (IOException e) {
            e.printStackTrace();
            MessageResponse errorResponse = new MessageResponse("file","upload_failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    public ResponseEntity<?> deletePost(UUID postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            MessageResponse errorResponse = new MessageResponse("authentication", "failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            MessageResponse errorResponse = new MessageResponse("authentication", "failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        User loggedInUser = userOptional.get();

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            MessageResponse errorResponse = new MessageResponse("post", "not_found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Post post = optionalPost.get();

        if (post.getUser().equals(loggedInUser)) {
            post.setStatus("delete");
        } else if (loggedInUser.getRoles().contains("ROLE_ADMIN")) {
            post.setStatus("admin_delete");
        } else {
            MessageResponse errorResponse = new MessageResponse("authorization", "denied");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        try {
            Post updatedPost = postRepository.save(post);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
            e.printStackTrace();
            MessageResponse errorResponse = new MessageResponse("status", "update_failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}

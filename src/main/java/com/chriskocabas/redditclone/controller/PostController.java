package com.chriskocabas.redditclone.controller;

import com.chriskocabas.redditclone.Exceptions.ValidationExceptions;
import com.chriskocabas.redditclone.dto.PostRequest;
import com.chriskocabas.redditclone.dto.PostResponse;
import com.chriskocabas.redditclone.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid  @RequestBody PostRequest postRequest, BindingResult bindingResult) {

        //check for validation errors
        Optional<String> validationErrors = ValidationExceptions.processValidationErrors(bindingResult);
        if (validationErrors.isPresent()) {
            System.out.print(validationErrors.get());
        }
            return status(HttpStatus.CREATED).body(postService.save(postRequest));
    }

    @PutMapping("/toggle-notifications/{id}")
    public ResponseEntity<Boolean>toggleNotifications(@PathVariable Long id, @RequestBody boolean newStatus){
        return status(HttpStatus.OK).body(postService.toggleNotificationStatus(id, newStatus));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

    @GetMapping("/subreddit-id/{subredditId}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long subredditId) {
        return status(HttpStatus.OK).body(postService.getPostsBySubreddit(subredditId));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username) {
        return status(HttpStatus.OK).body(postService.getPostsByUsername(username));
    }

    @PutMapping
    public ResponseEntity<PostResponse> UpdatePost(@Valid  @RequestBody PostRequest postRequest, BindingResult bindingResult) {

        //check for validation errors
        Optional<String> validationErrors = ValidationExceptions.processValidationErrors(bindingResult);
        if (validationErrors.isPresent()) {
            System.out.print(validationErrors.get());
        }

        return status(HttpStatus.CREATED).body(postService.update(postRequest));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePost(@Valid @RequestBody PostRequest postRequest, BindingResult bindingResult) {
        //check for validation errors
        Optional<String> validationErrors = ValidationExceptions.processValidationErrors(bindingResult);
        if (validationErrors.isPresent()) {
            System.out.print(validationErrors.get());
        }

        postService.delete(postRequest);
        return new ResponseEntity<>(OK);
    }

}
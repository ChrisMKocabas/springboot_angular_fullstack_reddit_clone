package com.chriskocabas.redditclone.controller;

import com.chriskocabas.redditclone.dto.CommentsDto;
import com.chriskocabas.redditclone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/comments")
@AllArgsConstructor
public class CommentsController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        commentService.save(commentsDto);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/post-id/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("/username/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName){
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForUser(userName));
    }

    @PutMapping()
    public ResponseEntity<Void> updateComment(@RequestBody CommentsDto commentsDto) {
        commentService.update(commentsDto);
        return new ResponseEntity<>(OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteComment(@RequestBody CommentsDto commentsDto) {
        commentService.delete(commentsDto);
        return new ResponseEntity<>(OK);
    }

}
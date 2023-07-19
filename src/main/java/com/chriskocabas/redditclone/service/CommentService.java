package com.chriskocabas.redditclone.service;

import com.chriskocabas.redditclone.dto.CommentsDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    public void save(CommentsDto commentsDto) {
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {

        //TODO implement this

        return Collections.emptyList();
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {

        //TODO implement this

        return Collections.emptyList();
    }
}

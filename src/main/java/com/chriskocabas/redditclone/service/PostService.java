package com.chriskocabas.redditclone.service;

import com.chriskocabas.redditclone.dto.PostRequest;
import com.chriskocabas.redditclone.dto.PostResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    public void save(PostRequest postRequest) {
    }

    public List<PostResponse> getAllPosts() {


        //TODO implement this

        return Collections.emptyList();
    }

    public PostResponse getPost(Long id) {

        //TODO implement this

        return new PostResponse();
    }

    public List<PostResponse> getPostsBySubreddit(Long subredditId) {

        //TODO implement this

        return Collections.emptyList();
    }

    public List<PostResponse> getPostsByUsername(String username) {

        //TODO implement this

        return Collections.emptyList();
    }
}

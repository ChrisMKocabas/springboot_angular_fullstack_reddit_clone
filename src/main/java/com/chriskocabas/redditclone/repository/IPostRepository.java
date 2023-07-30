package com.chriskocabas.redditclone.repository;

import com.chriskocabas.redditclone.model.Post;
import com.chriskocabas.redditclone.model.Subreddit;
import com.chriskocabas.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findAllByUser(User user);

}
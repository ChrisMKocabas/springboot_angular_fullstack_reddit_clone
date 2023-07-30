package com.chriskocabas.redditclone.repository;

import com.chriskocabas.redditclone.model.Comment;
import com.chriskocabas.redditclone.model.Post;
import com.chriskocabas.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findAllByUser(User user);

}

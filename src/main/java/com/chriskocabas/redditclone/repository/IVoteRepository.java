package com.chriskocabas.redditclone.repository;

import com.chriskocabas.redditclone.model.Post;
import com.chriskocabas.redditclone.model.User;
import com.chriskocabas.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

    void deleteVoteByPostAndUser(Post post, User currentUser);


}
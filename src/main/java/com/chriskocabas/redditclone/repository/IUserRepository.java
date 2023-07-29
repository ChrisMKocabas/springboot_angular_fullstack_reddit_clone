package com.chriskocabas.redditclone.repository;
import com.chriskocabas.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface IUserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}

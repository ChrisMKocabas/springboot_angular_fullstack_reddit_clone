package com.chriskocabas.redditclone.service;

import com.chriskocabas.redditclone.Exceptions.CustomException;
import com.chriskocabas.redditclone.dto.VoteDto;
import com.chriskocabas.redditclone.model.Post;
import com.chriskocabas.redditclone.model.Vote;
import com.chriskocabas.redditclone.repository.IPostRepository;
import com.chriskocabas.redditclone.repository.IVoteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.chriskocabas.redditclone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final IVoteRepository voteRepository;
    private final IPostRepository postRepository;
    private final AuthService authService;
    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(()->new CustomException("No posts found with id: "+ voteDto.getPostId()));
        Optional<Vote> voteByPostandUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(
                post,authService.getCurrentUser());

        if (voteByPostandUser.isPresent() && voteByPostandUser.get().getVoteType().equals(voteDto.getVoteType())){
            throw new CustomException("You have already "+ voteDto.getVoteType().toString().toLowerCase()+"d for this post");
        }

        if (UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount()+1);
        } else {
            post.setVoteCount(post.getVoteCount()-1);
        }

        voteRepository.save(mapVote(voteDto,post));
        postRepository.save(post);

    }

    private Vote mapVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}

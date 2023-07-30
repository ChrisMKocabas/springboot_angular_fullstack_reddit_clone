package com.chriskocabas.redditclone.service;

import com.chriskocabas.redditclone.Exceptions.CustomException;
import com.chriskocabas.redditclone.dto.SubredditDto;
import com.chriskocabas.redditclone.mapper.SubredditMapper;
import com.chriskocabas.redditclone.model.Post;
import com.chriskocabas.redditclone.model.Role;
import com.chriskocabas.redditclone.model.Subreddit;
import com.chriskocabas.redditclone.model.User;
import com.chriskocabas.redditclone.repository.ICommentRepository;
import com.chriskocabas.redditclone.repository.IPostRepository;
import com.chriskocabas.redditclone.repository.ISubredditRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final ISubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    private final AuthService authService;
    private final IPostRepository postRepository;
    private final ICommentRepository commentRepository;
    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit savedSubreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(savedSubreddit.getId());
        return subredditDto;
    }


    @Transactional
    public List<SubredditDto> getAll() {
        List<SubredditDto> allSubreddits = subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
        return allSubreddits;

    }


    @Transactional
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit= subredditRepository.findById(id).orElseThrow(
                ()->new CustomException("No subreddit found with given ID."));

        return subredditMapper.mapSubredditToDto(subreddit);

    }
    @Transactional
    public SubredditDto update(SubredditDto subredditDto) {

        User user = authService.getCurrentUser();

        if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MODERATOR)) {
            Subreddit subreddit = subredditRepository.findById(subredditDto.getId()).orElseThrow(
                    ()->new CustomException("Subreddit doesn't exist!"));
            subreddit.setDescription(subredditDto.getDescription());
            subreddit.setName(subredditDto.getName());
            return subredditMapper.mapSubredditToDto(subredditRepository.save(subreddit));
        } else {
            throw new CustomException("Can not update subreddit: insufficient privileges");
        }
    }

    @Transactional
    public void delete(SubredditDto subredditDto) {

        User user = authService.getCurrentUser();

        if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MODERATOR)) {
            Subreddit subreddit = subredditRepository.findById(subredditDto.getId()).orElseThrow(
                    ()->new CustomException("Subreddit doesn't exist!"));

            List<Post> posts = postRepository.findAllBySubreddit(subreddit);

            // Delete all comments belonging to the posts in the subreddit
            for (Post post : posts) {
                commentRepository.deleteAllByPost(post);
            }
            // Delete all posts in the subreddit
            postRepository.deleteAll(posts);

            subredditRepository.delete(subreddit);

        } else {
            throw new CustomException("Can not delete subreddit: insufficient privileges");
        }
    }
}

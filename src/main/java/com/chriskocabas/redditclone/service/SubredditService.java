package com.chriskocabas.redditclone.service;

import com.chriskocabas.redditclone.dto.SubredditDto;
import com.chriskocabas.redditclone.model.Subreddit;
import com.chriskocabas.redditclone.repository.ISubredditRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final ISubredditRepository subredditRepository;
    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit savedSubreddit = subredditRepository.save(mapSubredditDto(subredditDto));
        subredditDto.setId(savedSubreddit.getId());
        return subredditDto;
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
        return Subreddit.builder().name(subredditDto.getName())
                .description(subredditDto.getDescription())
                .build();
    }

    @Transactional
    public List<SubredditDto> getAll() {
        List<SubredditDto> allSubreddits = subredditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(toList());
        return allSubreddits;
                

    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        return SubredditDto.builder().name(subreddit.getName())
                .id(subreddit.getId())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }

    public SubredditDto getSubreddit(Long id) {


        //TODO implement this

        return new SubredditDto();
    }
}

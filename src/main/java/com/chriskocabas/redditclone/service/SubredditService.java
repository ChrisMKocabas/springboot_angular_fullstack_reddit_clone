package com.chriskocabas.redditclone.service;

import com.chriskocabas.redditclone.Exceptions.CustomException;
import com.chriskocabas.redditclone.dto.SubredditDto;
import com.chriskocabas.redditclone.mapper.SubredditMapper;
import com.chriskocabas.redditclone.model.Subreddit;
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
}

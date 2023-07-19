package com.chriskocabas.redditclone.service;

import com.chriskocabas.redditclone.dto.SubredditDto;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class SubredditService {
    public SubredditDto save(SubredditDto subredditDto) {

        //TODO implement this

        return new SubredditDto();
    }

    public List<SubredditDto> getAll() {

        //TODO implement this

        return Collections.emptyList();
    }

    public SubredditDto getSubreddit(Long id) {


        //TODO implement this

        return new SubredditDto();
    }
}

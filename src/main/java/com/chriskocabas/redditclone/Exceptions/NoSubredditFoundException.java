package com.chriskocabas.redditclone.Exceptions;

public class NoSubredditFoundException extends RuntimeException {
    public NoSubredditFoundException(String message) {
        super(message);
    }
}

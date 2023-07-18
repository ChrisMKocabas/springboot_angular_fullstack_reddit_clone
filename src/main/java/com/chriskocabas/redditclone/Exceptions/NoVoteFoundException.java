package com.chriskocabas.redditclone.Exceptions;

public class NoVoteFoundException extends RuntimeException {
    public NoVoteFoundException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public NoVoteFoundException(String exMessage) {
        super(exMessage);
    }
}
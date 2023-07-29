package com.chriskocabas.redditclone.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;

public class ValidationExceptions {

    public static Optional<String> processValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // If there are errors, create a custom error message containing all the error details
            StringBuilder errorMessage = new StringBuilder("Invalid request body. Errors: ");
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; ");
            }
            if (errorMessage.toString().length()>0) {
                return Optional.of(errorMessage.toString());
            }
        }
        return Optional.empty();
    }
}



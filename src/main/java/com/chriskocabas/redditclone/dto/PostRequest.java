package com.chriskocabas.redditclone.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private Long postId;
    private String subredditName;
    @NotBlank(message = "Post name cannot be empty or Null")
    private String postName;
    private String url;
    private String description;
}
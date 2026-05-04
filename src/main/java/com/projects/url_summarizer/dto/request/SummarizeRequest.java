package com.projects.url_summarizer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummarizeRequest {

    @NotBlank(message = "URL cannot be empty")
    @Pattern(regexp = "^https?://.*", message = "Must be a valid URL starting with http or https")
    private String url;
}
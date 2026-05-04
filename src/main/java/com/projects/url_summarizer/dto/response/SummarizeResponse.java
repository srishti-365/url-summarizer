package com.projects.url_summarizer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SummarizeResponse {

    private String url;
    private String summary;
}

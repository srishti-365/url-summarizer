package com.projects.url_summarizer.controller;

import com.projects.url_summarizer.dto.request.SummarizeRequest;
import com.projects.url_summarizer.dto.response.SummarizeResponse;
import com.projects.url_summarizer.service.SummarizerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SummarizerController {

    private final SummarizerService service;

    @PostMapping("/summarize")
    public ResponseEntity<SummarizeResponse> summarize(@Valid @RequestBody SummarizeRequest request){
        String pageText = service.fetchPageText(request.getUrl());
        String summary = service.summarize(pageText);
        return ResponseEntity.ok(new SummarizeResponse(request.getUrl(), summary));
    }
}

package com.projects.url_summarizer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SummarizerServiceTest{

    @InjectMocks
    private SummarizerService summarizerService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(summarizerService, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(summarizerService, "apiUrl", "https://api.groq.com/openai/v1/chat/completions");
        ReflectionTestUtils.setField(summarizerService, "model", "llama-3.3-70b-versatile");
    }

    @Test
    void fetchPageText_shouldReturnTextFromValidUrl() {
        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";

        String result = summarizerService.fetchPageText(url);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.length() > 100);
    }

    @Test
    void fetchPageText_shouldThrowExceptionForInvalidUrl(){
        String invalidUrl = "https://this-url-does-not-exist-xyz123.com";

        assertThrows(RuntimeException.class, () -> {
            summarizerService.fetchPageText(invalidUrl);
        });
    }
}

package com.projects.url_summarizer.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.micrometer.metrics.autoconfigure.MetricsProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class SummarizerService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.model}")
    private String model;

    private final WebClient webClient = WebClient.create();

    public String fetchPageText(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .referrer("https://www.google.com")
                    .timeout(10000)
                    .followRedirects(true)
                    .ignoreContentType(true)
                    .get();

            String text = doc.body().text();
            text = text.replaceAll("[^\\x20-\\x7E\\s]", "");
            return text;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch URL: " + e.getMessage());
        }
    }

    public String summarize(String pageText) {
        String trimmed = pageText.length() > 3000
                ? pageText.substring(0, 3000)
                : pageText;

        String requestBody = """
            {
                "model": "%s",
                "messages": [
                    {
                        "role": "system",
                        "content": "You are a helpful assistant. Summarize the given web page content clearly in 3-5 sentences."
                    },
                    {
                        "role": "user",
                        "content": "Summarize this: %s"
                    }
                ],
                "max_tokens": 500
            }
            """.formatted(model, trimmed.replace("\"", "'").replace("\n", " "));

        Map response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Groq error: " + body))
                )
                .bodyToMono(Map.class)
                .block();

        List<Map> choices = (List<Map>) response.get("choices");
        Map message = (Map) choices.get(0).get("message");
        return (String) message.get("content");
     }
}

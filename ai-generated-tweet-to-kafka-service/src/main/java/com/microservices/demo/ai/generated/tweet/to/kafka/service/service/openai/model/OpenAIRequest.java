package com.microservices.demo.ai.generated.tweet.to.kafka.service.service.openai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIRequest {
    private String model;
    private int max_completion_tokens;
    private double temperature;
    private List<Message> messages;

    @Data
    @Builder
    public static class Message {
        private String role;
        private List<Content> content;
    }

    @Data
    @Builder
    public static class Content {
        private String type;
        private String text;
    }
}

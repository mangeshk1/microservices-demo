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
public class OpenAIResponse {
    private String model;
    private Usage usage;
    private List<Choice> choices;

    @Data
    @Builder
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
    }

    @Data
    @Builder
    public static class Choice {
        private Message message;
    }

    @Data
    @Builder
    public static class Message {
        private String role;
        private String content;
    }
}

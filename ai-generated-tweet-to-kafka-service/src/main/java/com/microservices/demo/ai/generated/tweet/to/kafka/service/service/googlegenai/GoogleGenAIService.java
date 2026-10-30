package com.microservices.demo.ai.generated.tweet.to.kafka.service.service.googlegenai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.config.AIGeneratedTweetToKafkaServiceConfigData;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.exception.AIGeneratedTweetToKafkaServiceException;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
@Slf4j
@ConditionalOnProperty(name = "ai-generated-tweet-to-kafka-service.ai-service", havingValue = "GoogleGenAI")
public class GoogleGenAIService implements AIService {

    private final AIGeneratedTweetToKafkaServiceConfigData configData;
    private final Client googleGenAIClient;

    public GoogleGenAIService(AIGeneratedTweetToKafkaServiceConfigData configData) {
        this.configData = configData;
        this.googleGenAIClient = Client.builder()
                .apiKey(configData.getGoogleGenAI().getApiKey())
                .vertexAI(true)
                .build();
    }

    @PreDestroy
    public void close() {
        if(this.googleGenAIClient != null) {
            this.googleGenAIClient.close();
        }
    }

    @Override
    public String generateTweet() throws AIGeneratedTweetToKafkaServiceException {
        log.info("Generating tweet using Google GenAI Service");
        String prompt = configData.getPrompt().replace(configData.getKeywordsPlaceholder(), String.join(", ", configData.getStreamingDataKeywords()));
        return getPromptResponse(prompt);
    }

    private String getPromptResponse(String prompt) {
        GenerateContentConfig config = GenerateContentConfig.builder()
                .maxOutputTokens(configData.getGoogleGenAI().getMaxOutputTokens())
                .temperature(configData.getGoogleGenAI().getTemperature())
                .candidateCount(configData.getGoogleGenAI().getCandidateCount())
                .build();

        String modelName = configData.getGoogleGenAI().getModelName();
        GenerateContentResponse response =
                googleGenAIClient.models.generateContent(modelName, prompt, config);

        return response.text();
    }
}

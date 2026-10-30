package com.microservices.demo.ai.generated.tweet.to.kafka.service.service.openai;

import com.microservices.demo.ai.generated.tweet.to.kafka.service.config.AIGeneratedTweetToKafkaServiceConfigData;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.exception.AIGeneratedTweetToKafkaServiceException;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.service.AIService;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.service.openai.model.OpenAIRequest;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.service.openai.model.OpenAIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;


@Service
@Slf4j
@ConditionalOnProperty(name = "ai-generated-tweet-to-kafka-service.ai-service", havingValue = "OpenAI")
public class OpenAIService implements AIService {

    private final AIGeneratedTweetToKafkaServiceConfigData configData;
    private final ObjectMapper objectMapper;

    public OpenAIService(AIGeneratedTweetToKafkaServiceConfigData configData, ObjectMapper objectMapper) {
        this.configData = configData;
        this.objectMapper = objectMapper;
    }


    @Override
    public String generateTweet() throws AIGeneratedTweetToKafkaServiceException {
        log.info("Generating tweet using OpenAI Service");
        // Implementation for generating tweet using OpenAI
        String prompt = configData.getPrompt().replace(configData.getKeywordsPlaceholder(), String.join(", ", configData.getStreamingDataKeywords()));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Here you would implement the logic to send a request to the OpenAI API using the httpClient
            HttpPost request = getRequest(prompt);
            String response = httpClient.execute(request, httpResponse -> {
                int statusCode = httpResponse.getCode();
                String body = EntityUtils.toString(httpResponse.getEntity());
                if (statusCode < 200 || statusCode >= 300) {
                    throw new AIGeneratedTweetToKafkaServiceException(
                            "OpenAI API request failed with HTTP " + statusCode + ": " + body);
                }
                return body;
            });
            return parseResponse(response);
        } catch (IOException e) {
            throw new AIGeneratedTweetToKafkaServiceException("Failed while generating tweet using OpenAI", e);
        }
    }

    private HttpPost getRequest(String prompt) {
        // Create and return the HttpPost request with the necessary headers and body
        // This is a placeholder implementation; you would need to fill in the details
        HttpPost request = new HttpPost(configData.getOpenAI().getUrl());
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + configData.getOpenAI().getApiKey());
        request.addHeader(HttpHeaders.CONTENT_TYPE, configData.getOpenAI().getContentType());
        // Set the body of the request with the prompt and other parameters

        OpenAIRequest openAIRequest = OpenAIRequest.builder()
                .model(configData.getOpenAI().getModel())
                .max_completion_tokens(configData.getOpenAI().getMaxCompletionsTokens())
                .temperature(configData.getOpenAI().getTemperature())
                .messages(configData.getOpenAI().getMessages().stream()
                        .map(message -> OpenAIRequest.Message.builder()
                                .role(message.getRole())
                                .content(List.of(OpenAIRequest.Content.builder()
                                        .type(message.getContent().getFirst().getType())
                                        .text(prompt)
                                        .build()))
                                .build())
                        .toList())
                .build();

        request.setEntity(new StringEntity(objectMapper.writeValueAsString(openAIRequest)));
        return request;
    }

    private String parseResponse(String response) throws IOException {
        // Parse the response from OpenAI and extract the generated tweet
        OpenAIResponse openAIResponse = objectMapper.readValue(response, OpenAIResponse.class);
        if (openAIResponse.getChoices() == null || openAIResponse.getChoices().isEmpty()
                || openAIResponse.getChoices().getFirst().getMessage() == null) {
            throw new AIGeneratedTweetToKafkaServiceException(
                    "OpenAI response did not contain a message choice: " + response);
        }
        return openAIResponse.getChoices().getFirst().getMessage().getContent();
    }
}

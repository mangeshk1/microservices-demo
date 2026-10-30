package com.microservices.demo.ai.generated.tweet.to.kafka.service.runner;

import com.microservices.demo.ai.generated.tweet.to.kafka.service.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AIStreamRunner implements Runnable {

    private final AIService aiService;
    public AIStreamRunner(AIService aiService) {
        this.aiService = aiService;
    }

    @Override
    public void run() {
        String generatedTweet = aiService.generateTweet();
        log.info("Generated Tweet: {}", generatedTweet);
        // Logic to send the generated tweet to Kafka
    }
}

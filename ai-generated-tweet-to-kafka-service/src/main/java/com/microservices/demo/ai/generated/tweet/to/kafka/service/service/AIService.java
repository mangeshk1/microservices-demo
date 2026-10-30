package com.microservices.demo.ai.generated.tweet.to.kafka.service.service;

import com.microservices.demo.ai.generated.tweet.to.kafka.service.exception.AIGeneratedTweetToKafkaServiceException;

public interface AIService {

    String generateTweet() throws AIGeneratedTweetToKafkaServiceException;
}

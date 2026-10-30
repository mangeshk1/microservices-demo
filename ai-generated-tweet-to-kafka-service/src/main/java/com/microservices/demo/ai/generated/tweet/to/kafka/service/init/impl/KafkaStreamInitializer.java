package com.microservices.demo.ai.generated.tweet.to.kafka.service.init.impl;

import com.microservices.demo.ai.generated.tweet.to.kafka.service.init.StreamInitializer;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamInitializer implements StreamInitializer {
    @Override
    public boolean init() {
        // Implementation for initializing Kafka stream
        return true;
    }
}

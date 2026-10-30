package com.microservices.demo.ai.generated.tweet.to.kafka.service;

import com.microservices.demo.ai.generated.tweet.to.kafka.service.config.AIGeneratedTweetToKafkaServiceConfigData;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.exception.AIGeneratedTweetToKafkaServiceException;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.init.StreamInitializer;
import com.microservices.demo.ai.generated.tweet.to.kafka.service.runner.AIStreamRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Slf4j
@EnableScheduling
@ComponentScan(basePackages = "com.microservices.demo")
@SpringBootApplication
public class AIGeneratedTweetToKafkaServiceApplication implements CommandLineRunner {

	private final AIGeneratedTweetToKafkaServiceConfigData configData;
	private final StreamInitializer streamInitializer;
	private final AIStreamRunner aiStreamRunner;
	private final TaskScheduler taskScheduler;

    public AIGeneratedTweetToKafkaServiceApplication(AIGeneratedTweetToKafkaServiceConfigData configData, StreamInitializer streamInitializer, AIStreamRunner aiStreamRunner, TaskScheduler taskScheduler) {
        this.configData = configData;
        this.streamInitializer = streamInitializer;
        this.aiStreamRunner = aiStreamRunner;
        this.taskScheduler = taskScheduler;
    }

    public static void main(String[] args) {
		SpringApplication.run(AIGeneratedTweetToKafkaServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Application is starting...");
		boolean initResult = streamInitializer.init();
		if (initResult) {
			log.info("Starting AI stream runner with fixed rate of {} seconds", configData.getSchedulerDurationSec());
			taskScheduler.scheduleAtFixedRate(aiStreamRunner, Duration.of(configData.getSchedulerDurationSec(), ChronoUnit.SECONDS));
		} else {
			log.error("Stream initializer failed to initialize streams! Not starting the AI stream runner.");
			throw new AIGeneratedTweetToKafkaServiceException("Failed to initialize streams");
		}
	}
}

package com.insider.hackernews;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * This is where the application bootstraps and starts running
 * @author Mithun
 */
@SpringBootApplication
@EnableCaching
public class HackernewsApplication {

	private static final Logger logger = LoggerFactory.getLogger(HackernewsApplication.class);

	public static void main(String[] args) {
		logger.info("Starting the Hackernews application...");
		SpringApplication.run(HackernewsApplication.class, args);
	}
}

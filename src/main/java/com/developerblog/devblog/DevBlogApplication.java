package com.developerblog.devblog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevBlogApplication {
	private static final Logger log = LoggerFactory.getLogger(DevBlogApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DevBlogApplication.class, args);
		log.info("Application started!");
	}
}

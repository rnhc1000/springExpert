package com.devsuperior.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZonedDateTime;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {

		ZonedDateTime now = ZonedDateTime.now();
		int cores = Runtime.getRuntime().availableProcessors();
		String version = System.getProperty("java.version");

		if (logger.isInfoEnabled()) {
			logger.info("Validation App started at {}, powered by Java {}, running on top of {} cores", now, version, cores);
		}
	}
}

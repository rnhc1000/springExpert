package com.devsuperior.dscommerce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@SpringBootApplication
public class DscommerceApplication implements CommandLineRunner {

  public static final Logger logger = LoggerFactory.getLogger(DscommerceApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(DscommerceApplication.class, args);
  }

  /**
   * @param args noargs
   * @throws Exception RunTimeException
   */
  @Override
  public void run(String... args) throws Exception {

    final ZonedDateTime zonedDateTime = ZonedDateTime.now(ZonedDateTime.now().getZone());
    final String javaVersion = System.getProperty("java.version");
    final int numOfCores = Runtime.getRuntime().availableProcessors();

    if (logger.isInfoEnabled()) {
      logger.info("DSCommerce started running at zone {}, running java version {}, on top of {} cores",
          zonedDateTime,
          javaVersion,
          numOfCores
      );
    }

  }
}

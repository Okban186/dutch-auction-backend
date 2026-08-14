package com.auction.dutch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReverseApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReverseApplication.class, args);

  }

}

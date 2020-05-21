package com.michalkolos.covidscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CovidScraperApplication {

	public static void main(String[] args) { SpringApplication.run(CovidScraperApplication.class, args);
	}
}

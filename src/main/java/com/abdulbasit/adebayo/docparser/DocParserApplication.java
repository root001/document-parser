package com.abdulbasit.adebayo.docparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DocParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocParserApplication.class, args);
	}

	@Component
	public static class HealthCheckRunner implements ApplicationRunner {
		@Override
		public void run(ApplicationArguments args) throws Exception {
			if (args.containsOption("health")) {
				System.out.println("{\"status\": \"ok\"}");
				System.exit(0);
			}
		}
	}
}

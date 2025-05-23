package com.abdulbasit.adebayo.docparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class DocParserApplication {

	private static final Logger logger = LoggerFactory.getLogger(DocParserApplication.class);

	public static void main(String[] args) {
		logger.info("Starting DocParser with args: {}", (Object) args);
		if (args.length > 0 && ("--help".equals(args[0]) || "-h".equals(args[0]))) {
			printHelp();
			System.exit(0);
		}
		
		try {
			logger.info("Starting DocParser with args: {}", (Object) args);
			ConfigurableApplicationContext context = SpringApplication.run(DocParserApplication.class, args);
			
			DocumentProcessor processor = context.getBean(DocumentProcessor.class);
			processor.processDocuments();
			
			logger.info("Application completed successfully");
			System.exit(0);
		} catch (Exception e) {
			logger.error("Application failed", e);
			System.exit(1);
		}
	}

	private static void printHelp() {
		System.out.println("DocParser - Document Processing Tool");
		System.out.println("\nUsage:");
		System.out.println("  java -jar docparser.jar [config.yaml]");
		System.out.println("\nOptions:");
		System.out.println("  -h, --help    Show this help message");
		System.out.println("\nArguments:");
		System.out.println("  config.yaml   Path to configuration file (YAML format)");
		System.out.println("\nExample:");
		System.out.println("  java -jar docparser.jar config.yaml");
	}

	@Component
	public static class HealthCheckRunner implements ApplicationRunner {
		private static final Logger logger = LoggerFactory.getLogger(HealthCheckRunner.class);

		@Override
		public void run(ApplicationArguments args) throws Exception {
			if (args.containsOption("health")) {
				logger.info("Health check requested");
				System.out.println("{\"status\": \"ok\"}");
				System.exit(0);
			}
		}
	}
}

package com.abdulbasit.adebayo.docparser;

import com.abdulbasit.adebayo.docparser.parser.Orchestrator;
import com.abdulbasit.adebayo.docparser.writer.OutputWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class DocParserApplication {

    @Autowired
    private Orchestrator orchestrator;

    public static void main(String[] args) {
        SpringApplication.run(DocParserApplication.class, args);
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
    public class AppRunner implements ApplicationRunner {

        @Autowired
        private Orchestrator orchestrator;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            List<String> nonOptionArgs = args.getNonOptionArgs();

            if (args.containsOption("help") || args.containsOption("h") || nonOptionArgs.isEmpty()) {
                printHelp();
                return;
            }

            String configPath = nonOptionArgs.get(0);
            Double minPrice = null;
            Double maxPrice = null;

            if (args.containsOption("min-price")) {
                minPrice = parseDouble(args.getOptionValues("min-price").get(0));
            }

            if (args.containsOption("max-price")) {
                maxPrice = parseDouble(args.getOptionValues("max-price").get(0));
            }

            try {
                List<CarBrand> carBrands = orchestrator.process(configPath, minPrice, maxPrice);
                Path outputPath = Paths.get("output.json"); // You might want to configure this
                OutputWriter.writeJson(outputPath, carBrands);
                System.out.println("Data written to output.json");

            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private Double parseDouble(String value) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format: " + value);
                return null;
            }
        }
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

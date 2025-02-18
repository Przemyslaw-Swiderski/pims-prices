package ps.example.pimsprices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Prices microservice.
 * This class is responsible for bootstrapping the Spring Boot application.
 */
@SpringBootApplication
public class PimsPricesApplication {
    private static final Logger logger = LoggerFactory.getLogger(PimsPricesApplication.class);

    /**
     * Main method to start the application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        logger.info("Starting PimsPricesApplication...");
        SpringApplication.run(PimsPricesApplication.class, args);
    }
}
package ps.example.pimsprices.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ps.example.pimsprices.domain.Product;
import ps.example.pimsprices.repository.ProductRepository;

import java.util.Optional;
import java.util.UUID;
import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@EmbeddedKafka(brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@DirtiesContext()
public class ProductKafkaConsumerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testConsumeNewProductMessage() throws InterruptedException {
        String productId = String.valueOf(UUID.randomUUID());
        String message = String.format("{\"id\": \"%s\", \"name\": \"Smartphone\", \"ean\": \"9876543210987\"}", productId);
        kafkaTemplate.send("new-products", message);

        await()
                .atMost(5, SECONDS)
                .untilAsserted(() -> {
                    Optional<Product> savedProduct = productRepository.findById(productId);
                    assertTrue(savedProduct.isPresent(), "Product should be saved in the database");
                    assertEquals(productId, savedProduct.get().getId());
                    assertEquals("Smartphone", savedProduct.get().getName());
                    assertEquals("9876543210987", savedProduct.get().getEan());
                });
    }
}
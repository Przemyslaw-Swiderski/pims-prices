package ps.example.pimsprices.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import ps.example.pimsprices.domain.Product;
import ps.example.pimsprices.repository.ProductRepository;

@Service
public class ProductKafkaConsumer {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "new-products", groupId = "pims-prices-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            Product product = objectMapper.readValue(record.value(), Product.class);
            productRepository.save(product);
            System.out.println("Saved new product: " + product);
        } catch (Exception e) {
            System.err.println("Error processing product message: " + e.getMessage());
        }
    }
}

package ps.example.pimsprices.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.domain.Product;
import ps.example.pimsprices.repository.PriceRepository;
import ps.example.pimsprices.repository.ProductRepository;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class PriceServiceTest {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveAndRetrievePrice() {
        // Given
        Product product = new Product("88888888-fd7a-4a2b-89e4-5b3cddfcb49a", "nazwa", "9876543210987",null);
        productRepository.save(product);

        Price price = new Price(null, product, new BigDecimal("49.99"), "EUR", null);
        Price savedPrice = priceRepository.save(price);

        // When
        Price retrievedPrice = priceRepository.findById(savedPrice.getId()).orElse(null);

        // Then
        assertThat(retrievedPrice).isNotNull();
        assertThat(retrievedPrice.getProduct().getId()).isEqualTo("88888888-fd7a-4a2b-89e4-5b3cddfcb49a");
        assertThat(retrievedPrice.getPrice()).isEqualByComparingTo(new BigDecimal("49.99"));
        assertThat(retrievedPrice.getCurrency()).isEqualTo("EUR");
    }
}

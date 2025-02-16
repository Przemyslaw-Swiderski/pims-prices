package ps.example.pimsprices.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.domain.Product;
import ps.example.pimsprices.util.TestObjectGenerator;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class PriceRepositoryTest {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveAndRetrievePriceDynamicGeneratorWithObjectManualModification() {
        // Given
        Product product = new Product("88888888-fd7a-4a2b-89e4-5b3cddfcb49a", "nazwa", "9876543210987",null);
        productRepository.save(product);

        Price price = TestObjectGenerator.generate(Price.class);
        price.setProduct(product);

        // When
        Price savedPrice = priceRepository.save(price);
        Optional<Price> retrievedPrice = priceRepository.findById(savedPrice.getId());

        // Then
        assertThat(retrievedPrice).isPresent();
        assertThat(retrievedPrice.get().getProduct().getId()).isEqualTo(price.getProduct().getId());
        assertThat(retrievedPrice.get().getPrice()).isEqualByComparingTo(price.getPrice());
        assertThat(retrievedPrice.get().getCurrency()).isEqualTo(price.getCurrency());
    }

    @Test
    void shouldSaveAndRetrievePrice_ManualCase() {
        // Given
        Product product = new Product("88888888-fd7a-4a2b-89e4-5b3cddfcb49a", "nazwa", "9876543210987",null);
        productRepository.save(product);
        Price price = new Price(null, product, new BigDecimal("9999999.99"), "USD", null);

        // When
        Price savedPrice = priceRepository.save(price);
        Optional<Price> retrievedPrice = priceRepository.findById(savedPrice.getId());

        // Then
        assertThat(retrievedPrice).isPresent();
        assertThat(retrievedPrice.get().getProduct().getId()).isEqualTo("88888888-fd7a-4a2b-89e4-5b3cddfcb49a");
        assertThat(retrievedPrice.get().getPrice()).isEqualByComparingTo(new BigDecimal("9999999.99"));
        assertThat(retrievedPrice.get().getCurrency()).isEqualTo("USD");
    }
}

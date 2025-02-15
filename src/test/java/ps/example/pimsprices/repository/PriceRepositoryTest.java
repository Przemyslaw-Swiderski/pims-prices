package ps.example.pimsprices.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.util.TestObjectGenerator;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PriceRepositoryTest {

    @Autowired
    private PriceRepository priceRepository;

    @Test
    void shouldSaveAndRetrievePrice_DynamicGenerator() {
        // Given
        Price price = TestObjectGenerator.generate(Price.class);

        // When
        Price savedPrice = priceRepository.save(price);
        Optional<Price> retrievedPrice = priceRepository.findById(savedPrice.getId());

        // Then
        assertThat(retrievedPrice).isPresent();
        assertThat(retrievedPrice.get().getProductId()).isEqualTo(price.getProductId());
        assertThat(retrievedPrice.get().getPrice()).isEqualByComparingTo(price.getPrice());
        assertThat(retrievedPrice.get().getCurrency()).isEqualTo(price.getCurrency());
    }

    @Test
    void shouldSaveAndRetrievePrice_ManualCase() {
        // Given
        Price price = new Price(null, "DE-999999", new BigDecimal("9999999.99"), "USD", null);

        // When
        Price savedPrice = priceRepository.save(price);
        Optional<Price> retrievedPrice = priceRepository.findById(savedPrice.getId());

        // Then
        assertThat(retrievedPrice).isPresent();
        assertThat(retrievedPrice.get().getProductId()).isEqualTo("DE-999999");
        assertThat(retrievedPrice.get().getPrice()).isEqualByComparingTo(new BigDecimal("9999999.99"));
        assertThat(retrievedPrice.get().getCurrency()).isEqualTo("USD");
    }
}

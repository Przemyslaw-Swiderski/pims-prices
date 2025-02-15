package ps.example.pimsprices.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.repository.PriceRepository;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PriceServiceTest {

    @Autowired
    private PriceRepository priceRepository;

    @Test
    void shouldSaveAndRetrievePrice() {
        // Given
        Price price = new Price(null, "DE-098382", new BigDecimal("49.99"), "EUR", null);
        Price savedPrice = priceRepository.save(price);

        // When
        Price retrievedPrice = priceRepository.findById(savedPrice.getId()).orElse(null);

        // Then
        assertThat(retrievedPrice).isNotNull();
        assertThat(retrievedPrice.getProductId()).isEqualTo("DE-098382");
        assertThat(retrievedPrice.getPrice()).isEqualByComparingTo(new BigDecimal("49.99"));
        assertThat(retrievedPrice.getCurrency()).isEqualTo("EUR");
    }
}

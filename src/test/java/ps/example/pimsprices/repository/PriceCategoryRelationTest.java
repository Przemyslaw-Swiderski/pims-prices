package ps.example.pimsprices.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.domain.PricesCategory;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PriceCategoryRelationTest {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private PricesCategoryRepository pricesCategoryRepository;

    @Test
    void shouldSaveAndRetrievePriceWithCategories_ManualCase() {
        // Given
        PricesCategory retailCategory = new PricesCategory(null, "Retail Price", null);
        PricesCategory summerCategory = new PricesCategory(null, "Summer Price", null);
        pricesCategoryRepository.saveAll(Set.of(retailCategory, summerCategory));

        Price price = new Price(null, "DE-098382", new BigDecimal("49.99"), "EUR", Set.of(retailCategory, summerCategory));

        // When
        Price savedPrice = priceRepository.save(price);
        Price retrievedPrice = priceRepository.findById(savedPrice.getId()).orElse(null);

        // Then
        assertThat(retrievedPrice).isNotNull();
        assertThat(retrievedPrice.getPricesCategories()).hasSize(2);
    }
}

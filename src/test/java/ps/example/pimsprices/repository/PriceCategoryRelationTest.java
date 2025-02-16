package ps.example.pimsprices.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.domain.PricesCategory;
import ps.example.pimsprices.domain.Product;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class PriceCategoryRelationTest {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private PricesCategoryRepository pricesCategoryRepository;

    @Autowired
    private ProductRepository productRepository;


    @Test
    void shouldSaveAndRetrievePriceWithCategories_ManualCase() {
        // Given
        PricesCategory retailCategory = new PricesCategory(null, "Retail Price", null);
        PricesCategory summerCategory = new PricesCategory(null, "Summer Price", null);
        pricesCategoryRepository.saveAll(Set.of(retailCategory, summerCategory));

        Product product = new Product("88888888-fd7a-4a2b-89e4-5b3cddfcb49a", "nazwa", "9876543210987",null);
        productRepository.save(product);

        Price price = new Price(null, product, new BigDecimal("49.99"), "EUR", Set.of(retailCategory, summerCategory));

        // When
        Price savedPrice = priceRepository.save(price);
        Price retrievedPrice = priceRepository.findById(savedPrice.getId()).orElse(null);

        // Then
        assertThat(retrievedPrice).isNotNull();
        assertThat(retrievedPrice.getPricesCategories()).hasSize(2);
    }
}

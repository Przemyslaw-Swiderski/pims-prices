package ps.example.pimsprices.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ps.example.pimsprices.domain.PricesCategory;
import ps.example.pimsprices.util.TestObjectGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PricesCategoryRepositoryTest {

    @Autowired
    private PricesCategoryRepository pricesCategoryRepository;

    @Test
    void shouldSaveAndRetrievePricesCategory_DynamicGenerator() {
        // Given
        PricesCategory category = TestObjectGenerator.generate(PricesCategory.class);

        // When
        PricesCategory savedCategory = pricesCategoryRepository.save(category);
        PricesCategory retrievedCategory = pricesCategoryRepository.findById(savedCategory.getId()).orElse(null);

        // Then
        assertThat(retrievedCategory).isNotNull();
        assertThat(retrievedCategory.getName()).isEqualTo(category.getName());
    }

    @Test
    void shouldSaveAndRetrievePricesCategory_ManualCase() {
        // Given
        PricesCategory category = new PricesCategory(null, "Wholesale Price", null);

        // When
        PricesCategory savedCategory = pricesCategoryRepository.save(category);
        PricesCategory retrievedCategory = pricesCategoryRepository.findById(savedCategory.getId()).orElse(null);

        // Then
        assertThat(retrievedCategory).isNotNull();
        assertThat(retrievedCategory.getName()).isEqualTo("Wholesale Price");
    }
}

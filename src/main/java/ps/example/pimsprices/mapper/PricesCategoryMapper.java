package ps.example.pimsprices.mapper;

import org.springframework.stereotype.Component;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.domain.PricesCategory;
import ps.example.pimsprices.dto.PriceDTO;
import ps.example.pimsprices.dto.PricesCategoryDTO;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between PricesCategory entity and PricesCategoryDTO.
 */
@Component
public class PricesCategoryMapper {

    /**
     * Converts PricesCategory entity to PricesCategoryDTO.
     * @param category the PricesCategory entity
     * @return PricesCategoryDTO
     */
    public PricesCategoryDTO toPricesCategoryDTO(PricesCategory category) {
        return new PricesCategoryDTO(
                category.getId(),
                category.getName(),
                category.getPrices().stream()
                        .map(this::toPriceDTOWithoutCategories)
                        .collect(Collectors.toSet())
        );
    }

    /**
     * Converts PricesCategoryDTO to PricesCategory entity.
     * @param categoryDTO the PricesCategoryDTO
     * @return PricesCategory entity
     */
    public PricesCategory toEntity(PricesCategoryDTO categoryDTO, Set<Price> prices) {
        return PricesCategory.builder()
                .id(categoryDTO.id())
                .name(categoryDTO.name())
                .prices(prices)
                .build();
    }

    /**
     * Converts Price entity to PriceDTO without price details.
     * This method ensures that the returned DTO does not contain the set of prices categories.
     * @param price the Price entity
     * @return PriceDTO without prices categories details
     */
    public PriceDTO toPriceDTOWithoutCategories(Price price) {
        return new PriceDTO(price.getId(), price.getProduct().getId(), price.getPrice(), price.getCurrency(), Set.of());}
}

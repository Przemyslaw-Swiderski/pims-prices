package ps.example.pimsprices.mapper;

import org.springframework.stereotype.Component;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.domain.PricesCategory;
import ps.example.pimsprices.domain.Product;
import ps.example.pimsprices.dto.PriceDTO;
import ps.example.pimsprices.dto.PricesCategoryDTO;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between Price entity and PriceDTO.
 */
@Component
public class PriceMapper {

    /**
     * Converts Price entity to PriceDTO.
     * @param price the Price entity
     * @return PriceDTO
     */
    public PriceDTO toPriceDTO(Price price) {
        return new PriceDTO(
                price.getId(),
                price.getProduct().getId(),
                price.getPrice(),
                price.getCurrency(),
                price.getPricesCategories().stream()
                        .map(this::toPriceDTOWithoutPrices)
                        .collect(Collectors.toSet())
        );
    }

    /**
     * Converts PriceDTO to Price entity.
     * @param priceDTO the PriceDTO
     * @return Price entity
     */
    public Price toEntity(PriceDTO priceDTO, Product product, Set<PricesCategory> categories) {
        return Price.builder()
                .id(priceDTO.id())
                .product(product)
                .price(priceDTO.price())
                .currency(priceDTO.currency())
                .pricesCategories(categories)
                .build();
    }

    /**
     * Converts PricesCategory entity to PricesCategoryDTO without price details.
     * This method ensures that the returned DTO does not contain the set of prices.
     * @param category the PricesCategory entity
     * @return PricesCategoryDTO without price details
     */
    public PricesCategoryDTO toPriceDTOWithoutPrices(PricesCategory category) {
        return new PricesCategoryDTO(category.getId(), category.getName(), Set.of());
    }
}

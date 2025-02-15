package ps.example.pimsprices.mapper;

import org.springframework.stereotype.Component;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.domain.PricesCategory;
import ps.example.pimsprices.dto.PriceDTO;
import ps.example.pimsprices.dto.PricesCategoryDTO;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PriceMapper {

    public PriceDTO toPriceDTO(Price price) {
        return new PriceDTO(
                price.getId(),
                price.getProductId(),
                price.getPrice(),
                price.getCurrency(),
                price.getPricesCategories().stream()
                        .map(this::toPricesCategoryDTOWithoutPrices) // Unikamy nieskończonej serializacji
                        .collect(Collectors.toSet())
        );
    }

    public Price toEntity(PriceDTO priceDTO, Set<PricesCategory> categories) {
        return Price.builder()
                .id(priceDTO.id())
                .productId(priceDTO.productId())
                .price(priceDTO.price())
                .currency(priceDTO.currency())
                .pricesCategories(categories)
                .build();
    }

    public PricesCategoryDTO toPricesCategoryDTOWithoutPrices(PricesCategory category) {
        return new PricesCategoryDTO(category.getId(), category.getName(), Set.of()); // Pusta lista `prices`
    }
}

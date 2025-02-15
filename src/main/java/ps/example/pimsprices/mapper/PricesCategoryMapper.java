package ps.example.pimsprices.mapper;

import org.springframework.stereotype.Component;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.domain.PricesCategory;
import ps.example.pimsprices.dto.PriceDTO;
import ps.example.pimsprices.dto.PricesCategoryDTO;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PricesCategoryMapper {


    public PricesCategoryDTO toPricesCategoryDTO(PricesCategory category) {
        return new PricesCategoryDTO(
                category.getId(),
                category.getName(),
                category.getPrices().stream()
                        .map(this::toPriceDTOWithoutCategories) // Unikamy nieskończonej serializacji
                        .collect(Collectors.toSet())
        );
    }

    public PricesCategory toEntity(PricesCategoryDTO categoryDTO, Set<Price> prices) {
        return PricesCategory.builder()
                .id(categoryDTO.id())
                .name(categoryDTO.name())
                .prices(prices)
                .build();
    }

    public PriceDTO toPriceDTOWithoutCategories(Price price) {
        return new PriceDTO(price.getId(), price.getProductId(), price.getPrice(), price.getCurrency(), Set.of()); // Bez listy `categories`
    }
}

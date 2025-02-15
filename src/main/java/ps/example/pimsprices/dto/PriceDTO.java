package ps.example.pimsprices.dto;

import java.math.BigDecimal;
import java.util.Set;

public record PriceDTO(Long id, String productId, BigDecimal price, String currency, Set<PricesCategoryDTO> pricesCategories) {}

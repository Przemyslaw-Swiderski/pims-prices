package ps.example.pimsprices.dto;

import java.util.Set;

public record PricesCategoryDTO(Long id, String name, Set<PriceDTO> prices) {}

package ps.example.pimsprices.dto;

import java.util.List;
import java.util.Set;

public record ProductDTO(String id, String name, String ean, Set<PriceDTO> prices) {}
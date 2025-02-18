package ps.example.pimsprices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ps.example.pimsprices.dto.PricesCategoryDTO;
import ps.example.pimsprices.mapper.PricesCategoryMapper;
import ps.example.pimsprices.repository.PricesCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service  for managing price categories related operations.
 */
@Service
@RequiredArgsConstructor
public class PricesCategoryService {

    private final PricesCategoryRepository pricesCategoryRepository;
    private final PricesCategoryMapper pricesCategoryMapper;

    /**
     * Retrieves all prices categories.
     *
     * @return list of prices
     */
    @Transactional(readOnly = true)
    public List<PricesCategoryDTO> getAllPricesCategories() {
        return pricesCategoryRepository.findAll().stream()
                .map (pricesCategoryMapper::toPricesCategoryDTO)
                .collect(Collectors.toList());
    }
}

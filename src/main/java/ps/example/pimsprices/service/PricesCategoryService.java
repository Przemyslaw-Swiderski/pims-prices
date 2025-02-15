package ps.example.pimsprices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ps.example.pimsprices.dto.PricesCategoryDTO;
import ps.example.pimsprices.mapper.PricesCategoryMapper;
import ps.example.pimsprices.repository.PricesCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricesCategoryService {

    private final PricesCategoryRepository pricesCategoryRepository;
    private final PricesCategoryMapper pricesCategoryMapper;

    public List<PricesCategoryDTO> getAllPricesCategories() {
        return pricesCategoryRepository.findAll().stream()
                .map (pricesCategoryMapper::toPricesCategoryDTO)
                .collect(Collectors.toList());
    }
}

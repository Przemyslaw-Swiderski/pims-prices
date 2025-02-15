package ps.example.pimsprices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.dto.PriceDTO;
import ps.example.pimsprices.exception.PriceNotFoundException;
import ps.example.pimsprices.mapper.PriceMapper;
import ps.example.pimsprices.repository.PriceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;
    private final PriceMapper priceMapper;

    public List<PriceDTO> getAllPrices() {
        return priceRepository.findAll().stream()
                .map(priceMapper::toPriceDTO)
                .collect(Collectors.toList());
    }
    public Price getByProductId(String productId) {
        return priceRepository.findByProductId(productId)
                .orElseThrow(() -> new PriceNotFoundException("Price not found for product: " + productId));
    }

    public Price createPrice(Price Price) {
        return priceRepository.save(Price);
    }

    public Price updatePrice(String productId, BigDecimal newPrice) {
        Price existing = priceRepository.findByProductId(productId)
                .orElseThrow(() -> new PriceNotFoundException("Price not found for product: " + productId));
        existing.setPrice(newPrice);
        return priceRepository.save(existing);
    }

    public void deletePrice(String productId) {
        Price existing = priceRepository.findByProductId(productId)
                .orElseThrow(() -> new PriceNotFoundException("Price not found for product: " + productId));
        priceRepository.delete(existing);
    }
}


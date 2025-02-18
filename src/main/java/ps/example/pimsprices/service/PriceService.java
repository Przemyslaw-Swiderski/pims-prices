package ps.example.pimsprices.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.domain.PricesCategory;
import ps.example.pimsprices.domain.Product;
import ps.example.pimsprices.dto.PriceDTO;
import ps.example.pimsprices.exception.PriceNotFoundException;
import ps.example.pimsprices.mapper.PriceMapper;
import ps.example.pimsprices.repository.PriceRepository;
import ps.example.pimsprices.repository.PricesCategoryRepository;
import ps.example.pimsprices.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for handling price-related business logic.
 */
@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;
    private final PricesCategoryRepository pricesCategoryRepository;
    private final ProductRepository productRepository;
    private final PriceMapper priceMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String PRICE_TOPIC = "price-topic";

    /**
     * Retrieves all prices for all products.
     *
     * @return list of PriceDTOs
     */
    @Transactional(readOnly = true)
    public List<PriceDTO> getAllPrices() {
        List<Price> prices = priceRepository.findAll();
        if (prices.isEmpty()) {
            throw new PriceNotFoundException("No prices found");
        }
        return prices.stream()
                .map(priceMapper::toPriceDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all prices for a given product.
     *
     * @param productId the ID of the product
     * @return list of prices
     */
    @Transactional(readOnly = true)
    public List<PriceDTO> getPricesByProductId(String productId) {
        List<Price> prices = priceRepository.findByProductId(productId);
        if (prices.isEmpty()) {
            throw new PriceNotFoundException("No prices found for product: " + productId);
        }
        return prices.stream()
                .map(priceMapper::toPriceDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates new price.
     *
     * @param priceDTO with a details of new price
     */
    public PriceDTO createPrice(PriceDTO priceDTO) {
        Product product = productRepository.findById(priceDTO.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + priceDTO.productId()));
        Set<PricesCategory> categories = Optional.ofNullable(priceDTO.pricesCategories())
                .orElse(Collections.emptySet())
                .stream()
                .map(categoryDTO -> pricesCategoryRepository.findById(categoryDTO.id())
                        .orElseThrow(() -> new RuntimeException("Category not found: " + categoryDTO.id())))
                .collect(Collectors.toSet());
        Price newPrice = priceMapper.toEntity(priceDTO, product, categories);
        Price savedPrice = priceRepository.save(newPrice);

        String message = String.format("{\"event\":\"PRICE_CREATED\",\"productId\":\"%s\",\"price\":%.2f}",
                savedPrice.getProduct(), savedPrice.getPrice());
        kafkaTemplate.send(PRICE_TOPIC, message);

        return priceMapper.toPriceDTO(savedPrice);
    }

    /**
     * Modifies a price.
     *
     * @param priceId     the ID of the price
     * @param newPrice    new value of the price
     * @param categoryIds IDs of prices categories (not required)
     */
    public PriceDTO updatePrice(Long priceId, BigDecimal newPrice, Set<Long> categoryIds) {
        Price existingPrice = priceRepository.findById(priceId)
                .orElseThrow(() -> new PriceNotFoundException("Price with id " + priceId + " not found"));
        existingPrice.setPrice(newPrice);
        if (categoryIds != null && !categoryIds.isEmpty()) {
            Set<PricesCategory> categories = categoryIds.stream()
                    .map(id -> pricesCategoryRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Category with id: " + id + " not found")))
                    .collect(Collectors.toSet());
            existingPrice.setPricesCategories(categories);
        }
        Price updatedPrice = priceRepository.save(existingPrice);

        String message = String.format("{\"event\":\"PRICE_UPDATED\",\"productId\":\"%s\",\"price\":%.2f}",
                updatedPrice.getProduct(), updatedPrice.getPrice());
        kafkaTemplate.send(PRICE_TOPIC, message);

        return priceMapper.toPriceDTO(updatedPrice);
    }

    /**
     * Removes the price.
     *
     * @param priceId the ID of the price
     */
    public void deletePrice(Long priceId) {
        Price existingPrice = priceRepository.findById(priceId)
                .orElseThrow(() -> new PriceNotFoundException("Price with id: " + priceId + "not found"));
        priceRepository.delete(existingPrice);
    }
}


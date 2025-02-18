package ps.example.pimsprices.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.dto.PriceDTO;
import ps.example.pimsprices.service.PriceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Controller for managing price-related operations.
 */
@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
public class PriceController {

    private static final Logger logger = LoggerFactory.getLogger(PriceController.class);

    private final PriceService priceService;

    /**
     * Retrieves all prices for all products.
     *
     * @return list of prices
     */
    @GetMapping("/all")
    public List<PriceDTO> getAllPrices() {
        logger.info("Fetching all prices");
        return priceService.getAllPrices();
    }


    /**
     * Retrieves all prices for a given product.
     *
     * @param productId the ID of the product
     * @return list of prices
     */
    @GetMapping("/{productId}")
    public List<PriceDTO> getPrice(@PathVariable String productId) {
        logger.info("Fetching prices for product ID: {}", productId);
        return priceService.getPricesByProductId(productId);
    }

    /**
     * Creates new price.
     *
     * @param priceDTO with a details of new price
     */
    @PostMapping
    public PriceDTO createPrice(@RequestBody PriceDTO priceDTO) {
        logger.info("Creating new price");
        return priceService.createPrice(priceDTO);
    }

    /**
     * Modifies a price.
     *
     * @param priceId     the ID of the price
     * @param newPrice    new value of the price
     * @param categoryIds IDs of prices categories (not required)
     */
    @PutMapping("/{priceId}")
    public PriceDTO updatePrice(@PathVariable Long priceId, @RequestParam BigDecimal newPrice, @RequestParam(required = false) Set<Long> categoryIds) {
        logger.info("Updating price with ID: {}", priceId);
        return priceService.updatePrice(priceId, newPrice, categoryIds);
    }

    /**
     * Removes the price.
     *
     * @param priceId the ID of the price
     */
    @DeleteMapping("/{priceId}")
    public void deletePrice(@PathVariable Long priceId) {
        logger.info("Removing price with ID: {}", priceId);
        priceService.deletePrice(priceId);
    }
}


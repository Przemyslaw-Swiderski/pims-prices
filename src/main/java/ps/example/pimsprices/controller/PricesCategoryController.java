package ps.example.pimsprices.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ps.example.pimsprices.dto.PricesCategoryDTO;
import ps.example.pimsprices.service.PricesCategoryService;

import java.util.List;

/**
 * Controller for managing price categories related operations.
 */
@RestController
@RequestMapping("/api/v1/pricescategories")
@RequiredArgsConstructor
public class PricesCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(PriceController.class);

    private final PricesCategoryService pricesCategoryService;

    /**
     * Retrieves all prices categories.
     *
     * @return list of prices
     */
    @GetMapping("/all")
    public List<PricesCategoryDTO> getAllPrices() {
        logger.info("Fetching all prices categories");
        return pricesCategoryService.getAllPricesCategories();
    }

}


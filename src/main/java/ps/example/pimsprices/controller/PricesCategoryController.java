package ps.example.pimsprices.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ps.example.pimsprices.dto.PricesCategoryDTO;
import ps.example.pimsprices.service.PricesCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pricescategories")
@RequiredArgsConstructor
public class PricesCategoryController {

    private final PricesCategoryService pricesCategoryService;

    @GetMapping("/all")
    public List<PricesCategoryDTO> getAllPrices(){
        return pricesCategoryService.getAllPricesCategories();
    }

}


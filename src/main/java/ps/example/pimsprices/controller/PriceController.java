package ps.example.pimsprices.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.dto.PriceDTO;
import ps.example.pimsprices.service.PriceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @GetMapping("/all")
    public List<PriceDTO> getAllPrices(){
        return priceService.getAllPrices();
    }

    @GetMapping("/{productId}")
    public List<PriceDTO> getPrice(@PathVariable String productId) {
        return priceService.getPricesByProductId(productId);
    }

    @PostMapping
    public PriceDTO createPrice(@RequestBody PriceDTO priceDTO) {
        return priceService.createPrice(priceDTO);
    }

    @PutMapping("/{priceId}")
    public PriceDTO updatePrice(@PathVariable Long priceId, @RequestParam BigDecimal newPrice, @RequestParam(required = false) Set<Long> categoryIds) {
        return priceService.updatePrice(priceId, newPrice, categoryIds);
    }

    @DeleteMapping("/{priceId}")
    public void deletePrice(@PathVariable Long priceId) {
        priceService.deletePrice(priceId);
    }
}


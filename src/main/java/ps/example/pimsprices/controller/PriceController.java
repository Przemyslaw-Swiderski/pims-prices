package ps.example.pimsprices.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.dto.PriceDTO;
import ps.example.pimsprices.service.PriceService;

import java.math.BigDecimal;
import java.util.List;

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
    public Price getPrice(@PathVariable String productId) {
        return priceService.getByProductId(productId);
    }

    @PostMapping
    public Price createPrice(@RequestBody Price Price) {
        return priceService.createPrice(Price);
    }

    @PutMapping("/{productId}")
    public Price updatePrice(@PathVariable String productId, @RequestParam BigDecimal newPrice) {
        return priceService.updatePrice(productId, newPrice);
    }

    @DeleteMapping("/{productId}")
    public void deletePrice(@PathVariable String productId) {
        priceService.deletePrice(productId);
    }
}


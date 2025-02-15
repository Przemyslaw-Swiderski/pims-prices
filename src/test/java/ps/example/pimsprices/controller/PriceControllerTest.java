package ps.example.pimsprices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.dto.PriceDTO;
import ps.example.pimsprices.service.PriceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PriceController.class)
class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PriceService priceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllPrices() throws Exception {

        // Given
        List<PriceDTO> prices = List.of(
                new PriceDTO(1L, "DE-098382", new BigDecimal("19.99"), "EUR", Set.of()),
                new PriceDTO(2L, "PL-098382", new BigDecimal("82.11"), "PLN", Set.of())
        );

        // When
        when(priceService.getAllPrices()).thenReturn(prices);

        // Then
        mockMvc.perform(get("/api/v1/prices/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].productId", is("DE-098382")))
                .andExpect(jsonPath("$[1].productId", is("PL-098382")));
    }

    @Test
    void shouldReturnPriceByProductId() throws Exception {

        // Given
        Price price = new Price(1L, "DE-098382", new BigDecimal("19.99"), "EUR", Set.of());

        // When
        when(priceService.getByProductId("DE-098382")).thenReturn(price);

        // Then
        mockMvc.perform(get("/api/v1/prices/DE-098382"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is("DE-098382")))
                .andExpect(jsonPath("$.price", is(19.99)))
                .andExpect(jsonPath("$.currency", is("EUR")));
    }

    @Test
    void shouldCreateNewPrice() throws Exception {

        // Given
        Price price = new Price(null, "DE-123456", new BigDecimal("25.50"), "USD", Set.of());
        Price savedPrice = new Price(3L, "DE-123456", new BigDecimal("25.50"), "USD", Set.of());

        // When
        when(priceService.createPrice(any(Price.class))).thenReturn(savedPrice);

        // Then
        mockMvc.perform(post("/api/v1/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(price)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.productId", is("DE-123456")))
                .andExpect(jsonPath("$.price", is(25.50)))
                .andExpect(jsonPath("$.currency", is("USD")));
    }

    @Test
    void shouldUpdatePrice() throws Exception {

        // Given
        Price updatedPrice = new Price(1L, "DE-098382", new BigDecimal("29.99"), "EUR", Set.of());

        // When
        when(priceService.updatePrice(eq("DE-098382"), any(BigDecimal.class))).thenReturn(updatedPrice);

        // Then
        mockMvc.perform(put("/api/v1/prices/DE-098382")
                        .param("newPrice", "29.99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(29.99)));
    }

    @Test
    void shouldDeletePrice() throws Exception {

        // Given
        doNothing().when(priceService).deletePrice("DE-098382");

        // When
        mockMvc.perform(delete("/api/v1/prices/DE-098382"))
                .andExpect(status().isOk());

        // Then
        verify(priceService, times(1)).deletePrice("DE-098382");
    }
}

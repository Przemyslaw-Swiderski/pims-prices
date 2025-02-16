package ps.example.pimsprices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ps.example.pimsprices.dto.PriceDTO;
import ps.example.pimsprices.service.PriceService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
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
                new PriceDTO(1L, "88888888-fd7a-4a2b-89e4-5b3cddfcb49a", new BigDecimal("19.99"), "EUR", Set.of()),
                new PriceDTO(2L, "88888889-fd7a-4a2b-89e4-5b3cddfcb49a", new BigDecimal("82.11"), "PLN", Set.of())
        );

        // When
        when(priceService.getAllPrices()).thenReturn(prices);

        // Then
        mockMvc.perform(get("/api/v1/prices/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].productId", is("88888888-fd7a-4a2b-89e4-5b3cddfcb49a")))
                .andExpect(jsonPath("$[1].productId", is("88888889-fd7a-4a2b-89e4-5b3cddfcb49a")));
    }

    @Test
    void shouldReturnPriceByProductId() throws Exception {

        // Given
        PriceDTO priceDTO = new PriceDTO(1L, "88888888-fd7a-4a2b-89e4-5b3cddfcb49a", new BigDecimal("19.99"), "EUR", Set.of());
        List<PriceDTO> pricesDTO = new ArrayList<>();;
        pricesDTO.add(priceDTO);

        // When
        when(priceService.getPricesByProductId("88888888-fd7a-4a2b-89e4-5b3cddfcb49a")).thenReturn(pricesDTO);

        // Then
        mockMvc.perform(get("/api/v1/prices/88888888-fd7a-4a2b-89e4-5b3cddfcb49a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId", is("88888888-fd7a-4a2b-89e4-5b3cddfcb49a")))
                .andExpect(jsonPath("$[0].price", is(19.99)))
                .andExpect(jsonPath("$[0].currency", is("EUR")));
    }

    @Test
    void shouldCreateNewPrice() throws Exception {

        // Given
        PriceDTO priceDTO = new PriceDTO(null, "88888888-fd7a-4a2b-89e4-5b3cddfcb49a", new BigDecimal("25.50"), "USD", Set.of());
        PriceDTO savedPriceDTO = new PriceDTO(3L, "88888888-fd7a-4a2b-89e4-5b3cddfcb49a", new BigDecimal("25.50"), "USD", Set.of());

        // When
        when(priceService.createPrice(any(PriceDTO.class))).thenReturn(savedPriceDTO);

        // Then
        mockMvc.perform(post("/api/v1/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(priceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.productId", is("88888888-fd7a-4a2b-89e4-5b3cddfcb49a")))
                .andExpect(jsonPath("$.price", is(25.50)))
                .andExpect(jsonPath("$.currency", is("USD")));
    }

    @Test
    void shouldUpdatePrice() throws Exception {

        // Given
        PriceDTO updatedPriceDTO = new PriceDTO(1L, "88888888-fd7a-4a2b-89e4-5b3cddfcb49a", new BigDecimal("29.99"), "EUR", Set.of());

        // When
        when(priceService.updatePrice(eq(1L), any(BigDecimal.class), any())).thenReturn(updatedPriceDTO);

        // Then
        mockMvc.perform(put("/api/v1/prices/1")
               .param("newPrice", "29.99"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.price", is(29.99)));
    }

    @Test
    void shouldDeletePrice() throws Exception {

        // Given
        doNothing().when(priceService).deletePrice(1L);

        // When
        mockMvc.perform(delete("/api/v1/prices/1"))
                .andExpect(status().isOk());

        // Then
        verify(priceService, times(1)).deletePrice(1L);
    }
}

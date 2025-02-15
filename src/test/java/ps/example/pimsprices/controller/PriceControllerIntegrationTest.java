package ps.example.pimsprices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ps.example.pimsprices.domain.Price;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PriceControllerIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndRetrievePrice() throws Exception {
        // Given
        Price price = new Price(null, "DE-123456", new BigDecimal("25.50"), "USD", null);
        String json = objectMapper.writeValueAsString(price);

        // When
        mockMvc.perform(post("/api/v1/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        // Then
        mockMvc.perform(get("/api/v1/prices/DE-123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("DE-123456"))
                .andExpect(jsonPath("$.price").value(25.50));
    }
}

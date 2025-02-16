package ps.example.pimsprices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ps.example.pimsprices.domain.Price;
import ps.example.pimsprices.domain.Product;
import ps.example.pimsprices.dto.PriceDTO;
import ps.example.pimsprices.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PriceControllerIntegrationTest{

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldCreateAndRetrievePrice() throws Exception {
        // Given
        Product product = new Product("88888888-fd7a-4a2b-89e4-5b3cddfcb49a", "nazwa", "9876543210987",null);
        productRepository.save(product);

        PriceDTO priceDTO = new PriceDTO(null, "88888888-fd7a-4a2b-89e4-5b3cddfcb49a", new BigDecimal("25.50"), "USD", null);
        String json = objectMapper.writeValueAsString(priceDTO);

        // When
        mockMvc.perform(post("/api/v1/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        // Then
        mockMvc.perform(get("/api/v1/prices/88888888-fd7a-4a2b-89e4-5b3cddfcb49a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productId", is("88888888-fd7a-4a2b-89e4-5b3cddfcb49a")))
                .andExpect(jsonPath("$[0].price", is(25.50)))
                .andExpect(jsonPath("$[0].currency", is("USD")));
    }
}

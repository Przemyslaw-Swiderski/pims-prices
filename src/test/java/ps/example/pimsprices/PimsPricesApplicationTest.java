package ps.example.pimsprices;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class PimsPricesApplicationTest {

    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }
}
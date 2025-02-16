package ps.example.pimsprices.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Price")
@Table(name = "prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // price as BigDecimal in order to prevent calculation inaccuracies, especially rounding errors.
    private BigDecimal price;
    private String currency;

    @ManyToMany
    @JoinTable(
            name = "prices_and_prices_categories",
            joinColumns = @JoinColumn(name = "price_id"),
            inverseJoinColumns = @JoinColumn(name = "prices_category_id"))
    private Set<PricesCategory> pricesCategories = new HashSet<>();
}

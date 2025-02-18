package ps.example.pimsprices.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a price associated with a product.
 * This class manages pricing details, including currency and category associations.
 */
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

    /**
     * Associated product in a price many-to-one product relationship.
     */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Price value stored as BigDecimal to ensure precision and avoid rounding errors.
     */
    private BigDecimal price;

    /**
     * Currency code for the price (e.g., USD, EUR).
     */
    private String currency;

    /**
     * Categories associated with this price, allowing a many-to-many relationship.
     */
    @ManyToMany
    @JoinTable(
            name = "prices_and_prices_categories",
            joinColumns = @JoinColumn(name = "price_id"),
            inverseJoinColumns = @JoinColumn(name = "prices_category_id"))
    private Set<PricesCategory> pricesCategories = new HashSet<>();
}

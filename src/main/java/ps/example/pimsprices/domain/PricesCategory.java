package ps.example.pimsprices.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a category of prices.
 * Categories can be associated with multiple prices in a many-to-many relationship.
 */
@Entity(name="PricesCategory")
@Table(name = "prices_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricesCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the price category.
     */
    private String name;

    /**
     * Prices associated with this category in a many-to-many relationship.
     */
    @ManyToMany(mappedBy = "pricesCategories")
    private Set<Price> prices = new HashSet<>();
}

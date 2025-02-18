package ps.example.pimsprices.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity representing a product.
 * Prices can be associated in a many-to-one product relationship.
 */
@Entity(name="Product")
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @Column(updatable = false, nullable = false)
    private String id;

    /**
     * Name of the product.
     */
    private String name;

    /**
     * Ean of the product.
     */
    private String ean;

    /**
     * Prices associated with this product in a product one-to-many prices relationship.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Price> prices = new HashSet<>();
}


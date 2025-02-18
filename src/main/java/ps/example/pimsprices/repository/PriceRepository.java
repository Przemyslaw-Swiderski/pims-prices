package ps.example.pimsprices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ps.example.pimsprices.domain.Price;
import java.util.List;


/**
 * Repository for managing Price entities.
 * This repository handles database interactions for the Price entity.
 */
@Repository
@Transactional
public interface PriceRepository extends JpaRepository<Price, Long> {
    /**
     * Finds all prices associated with a specific product ID.
     * @param productId the ID of the product
     * @return list of prices
     */
    List<Price> findByProductId(String productId);
}
package ps.example.pimsprices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.example.pimsprices.domain.Price;
import java.util.List;


@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByProductId(String productId);
}
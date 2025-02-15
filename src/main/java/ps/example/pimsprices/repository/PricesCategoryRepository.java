package ps.example.pimsprices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.example.pimsprices.domain.PricesCategory;

@Repository
public interface PricesCategoryRepository extends JpaRepository<PricesCategory, Long> {
}
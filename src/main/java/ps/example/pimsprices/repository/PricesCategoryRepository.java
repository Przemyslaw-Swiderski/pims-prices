package ps.example.pimsprices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ps.example.pimsprices.domain.PricesCategory;

@Repository
@Transactional
public interface PricesCategoryRepository extends JpaRepository<PricesCategory, Long> {
}
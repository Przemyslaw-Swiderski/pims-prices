package ps.example.pimsprices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ps.example.pimsprices.domain.Product;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, String> {
}
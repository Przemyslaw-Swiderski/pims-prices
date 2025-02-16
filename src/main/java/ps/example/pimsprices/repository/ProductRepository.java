package ps.example.pimsprices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ps.example.pimsprices.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
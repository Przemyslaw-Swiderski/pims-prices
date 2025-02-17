package ps.example.pimsprices.security.repositories;

import ps.example.pimsprices.security.entities.RefreshToken;
import ps.example.pimsprices.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String requestRefreshToken);

}
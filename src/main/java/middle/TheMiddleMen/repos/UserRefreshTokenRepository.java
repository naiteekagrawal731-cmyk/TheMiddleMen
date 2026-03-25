package middle.TheMiddleMen.repos;

import middle.TheMiddleMen.entities.User;
import middle.TheMiddleMen.entities.tokens.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, UUID> {

    Optional<UserRefreshToken> findByToken(UUID token);

    void deleteByUser(User user);

    Optional<UserRefreshToken> findByUser(User user);
}

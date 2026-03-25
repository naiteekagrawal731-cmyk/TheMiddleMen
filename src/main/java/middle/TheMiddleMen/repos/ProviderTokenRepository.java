package middle.TheMiddleMen.repos;

import middle.TheMiddleMen.entities.UserIdentity;
import middle.TheMiddleMen.entities.tokens.ProviderToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProviderTokenRepository extends JpaRepository<ProviderToken, UUID> {

    Optional<ProviderToken> findByUserIdentity(UserIdentity userIdentity);
}

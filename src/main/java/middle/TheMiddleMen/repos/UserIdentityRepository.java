package middle.TheMiddleMen.repos;

import middle.TheMiddleMen.entities.User;
import middle.TheMiddleMen.entities.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserIdentityRepository extends JpaRepository<UserIdentity, UUID> {

    List<UserIdentity> findAllByUser(User user);

    @Query(value = "SELECT * FROM user_identity where provider = :provider and sub =:sub",nativeQuery = true)
    public Optional<UserIdentity> findByProviderAndSub(@Param("provider") String provider,@Param("sub") String sub);

    @Query(value = """
    SELECT *
    FROM user_identity
    WHERE `user_id` = :userId
    AND provider = :provider
""", nativeQuery = true)
    Optional<UserIdentity> findByUserAndProvider(
            @Param("userId") UUID userId,
            @Param("provider") String provider
    );

}

package middle.TheMiddleMen.repos;

import middle.TheMiddleMen.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.rmi.server.UID;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, UID> {

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);
}

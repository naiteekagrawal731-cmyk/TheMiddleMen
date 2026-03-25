package middle.TheMiddleMen.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "user_identity",
        uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "sub"})
)
public class UserIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

    private String provider;//local,google, github etc

    private String sub;//id of user inside the provider system


}

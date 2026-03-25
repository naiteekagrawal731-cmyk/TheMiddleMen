package middle.TheMiddleMen.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private Instant createdAt;

    private Instant updatedAt;

    private String password;


    @PrePersist
    public void setCreatedAt(){
        createdAt = Instant.now();
    }
    @PreUpdate
    public void setUpdatedAt(){
        updatedAt = Instant.now();
    }



}

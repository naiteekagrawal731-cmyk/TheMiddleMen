package middle.TheMiddleMen.dtos.requestDtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {

    private String username;
    private String password;
}

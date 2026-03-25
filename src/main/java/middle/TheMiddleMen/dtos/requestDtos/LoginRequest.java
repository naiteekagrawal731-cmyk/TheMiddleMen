package middle.TheMiddleMen.dtos.requestDtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    String username;

    String password;
}

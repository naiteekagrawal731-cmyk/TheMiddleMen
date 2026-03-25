package middle.TheMiddleMen.services;

import middle.TheMiddleMen.dtos.requestDtos.RegistrationRequest;
import middle.TheMiddleMen.dtos.responseDtos.RegistrationResponse;
import middle.TheMiddleMen.repos.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<RegistrationResponse> register(RegistrationRequest registrationRequest){
        String username = registrationRequest.getUsername();
        String password = registrationRequest.getPassword();

        userService.createUserByUsernameAndPassword(username,passwordEncoder.encode(password));

        return ResponseEntity.status(201).body(RegistrationResponse.builder().message("Registration Complete").build());
    }
}

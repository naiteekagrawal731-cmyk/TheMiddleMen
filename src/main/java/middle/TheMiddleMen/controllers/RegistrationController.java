package middle.TheMiddleMen.controllers;

import middle.TheMiddleMen.dtos.requestDtos.RegistrationRequest;
import middle.TheMiddleMen.dtos.responseDtos.RegistrationResponse;
import middle.TheMiddleMen.services.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest registrationRequest){
        return registrationService.register(registrationRequest);
    }
}

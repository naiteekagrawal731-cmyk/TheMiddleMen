package middle.TheMiddleMen.controllers;

import lombok.extern.slf4j.Slf4j;
import middle.TheMiddleMen.dtos.requestDtos.LoginRequest;
import middle.TheMiddleMen.services.UsernamePasswordLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@Slf4j
public class LoginController {

    private final UsernamePasswordLoginService usernamePasswordLoginService;

    public LoginController(UsernamePasswordLoginService usernamePasswordLoginService) {
        this.usernamePasswordLoginService = usernamePasswordLoginService;
    }

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){

        return usernamePasswordLoginService.login(loginRequest);
    }
}

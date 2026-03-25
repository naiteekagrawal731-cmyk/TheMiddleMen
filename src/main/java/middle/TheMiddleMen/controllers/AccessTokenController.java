package middle.TheMiddleMen.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import middle.TheMiddleMen.dtos.responseDtos.AccessTokenResponse;
import middle.TheMiddleMen.exceptions.customExceptions.InvalidToken;
import middle.TheMiddleMen.services.UserAccessTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/auth/token")
@Slf4j
public class AccessTokenController {

    private final UserAccessTokenService userAccessTokenService;

    public AccessTokenController(UserAccessTokenService userAccessTokenService) {
        this.userAccessTokenService = userAccessTokenService;
    }


    @GetMapping
    public ResponseEntity<AccessTokenResponse> getAccessToken(HttpServletRequest request){
        //Getting refresh token from request cookie
        Optional<Cookie> refreshToken =  Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("refresh_token")).findFirst();
        if(refreshToken.isEmpty()){
            throw new InvalidToken("Refresh token is not present");
        }

        return userAccessTokenService.getAccessToken(UUID.fromString(refreshToken.get().getValue()));
    }

}

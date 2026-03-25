package middle.TheMiddleMen.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import middle.TheMiddleMen.dtos.requestDtos.LoginRequest;
import middle.TheMiddleMen.entities.User;
import middle.TheMiddleMen.exceptions.customExceptions.UsernameNotFound;
import middle.TheMiddleMen.security.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UsernamePasswordLoginService {

    private final UserRefreshTokenService userRefreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final HttpServletResponse response;

    public UsernamePasswordLoginService(UserRefreshTokenService userRefreshTokenService, AuthenticationManager authenticationManager, UserService userService, HttpServletResponse response) {
        this.userRefreshTokenService = userRefreshTokenService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.response = response;
    }


    public ResponseEntity<String> login(LoginRequest loginRequest){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        User user = userService.getUserByUsername(username).orElseThrow(() -> new UsernameNotFound("User with username = "+username+" does not exist"));

        String refreshToken = String.valueOf(userRefreshTokenService.generateRefreshToken(user));

        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        response.addCookie(refreshTokenCookie);
        refreshTokenCookie.isHttpOnly();
        refreshTokenCookie.setPath("/");

        return ResponseEntity.ok("Login SuccessFull");
    }
}

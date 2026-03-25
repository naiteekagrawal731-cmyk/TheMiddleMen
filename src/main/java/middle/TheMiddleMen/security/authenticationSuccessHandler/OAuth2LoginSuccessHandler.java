package middle.TheMiddleMen.security.authenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import middle.TheMiddleMen.services.OAuth2LoginService;
import middle.TheMiddleMen.services.UserIdentityService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthorizedClientService clientService;
    private final UserIdentityService userIdentityService;
    private final OAuth2LoginService oAuth2LoginService;

    public OAuth2LoginSuccessHandler(OAuth2AuthorizedClientService clientService, UserIdentityService userIdentityService, OAuth2LoginService oAuth2LoginService) {
        this.clientService = clientService;
        this.userIdentityService = userIdentityService;
        this.oAuth2LoginService = oAuth2LoginService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;

        String provider = auth.getAuthorizedClientRegistrationId();
        String sub = auth.getName();

        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(provider,sub);

        Map<String,Object> attributes = auth.getPrincipal().getAttributes();

        String email = attributes.get("email").toString();
        OAuth2AccessToken accessToken = client.getAccessToken();
        OAuth2RefreshToken refreshToken = (client.getRefreshToken() != null) ? client.getRefreshToken() : null;

        userIdentityService.createNewUserIdentity(email,accessToken,refreshToken,provider,sub);

        Cookie[] cookies = request.getCookies();
        String frontendRedirect = "";
        for(Cookie i : cookies){
            if(i.getName().equals("frontend_redirect")){
                // Decoding the url
                frontendRedirect = URLDecoder.decode(i.getValue(), StandardCharsets.UTF_8);
                break;
            }
        }
        String token = oAuth2LoginService.getRefreshToken(auth).toString();

        Cookie refreshTokenCookie = new Cookie("refresh_token",token);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(3600);
        refreshTokenCookie.isHttpOnly();

        response.addCookie(refreshTokenCookie);
        response.sendRedirect(frontendRedirect);

    }
}

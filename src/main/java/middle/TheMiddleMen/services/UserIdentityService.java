package middle.TheMiddleMen.services;

import middle.TheMiddleMen.entities.User;
import middle.TheMiddleMen.entities.UserIdentity;
import middle.TheMiddleMen.entities.tokens.ProviderToken;
import middle.TheMiddleMen.repos.UserIdentityRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserIdentityService {

    private final UserService userService;
    private final UserIdentityRepository userIdentityRepository;
    private final ProviderTokenService providerTokenService;

    public UserIdentityService(UserService userService, UserIdentityRepository userIdentityRepository, ProviderTokenService providerTokenService) {
        this.userService = userService;
        this.userIdentityRepository = userIdentityRepository;
        this.providerTokenService = providerTokenService;
    }

    public void createNewUserIdentity(String email, OAuth2AccessToken accessToken, OAuth2RefreshToken refreshToken,String provider,String sub){
        Optional<User> user = userService.getUserByEmail(email);
        if(user.isEmpty()){
            userService.createUserByEmai(email);
            user = userService.getUserByEmail(email);
        }
        List<UserIdentity> userIdentities = userIdentityRepository.findAllByUser(user.get());
        for(UserIdentity ui : userIdentities){
            if(ui.getProvider().equals(provider)){
                providerTokenService.createNewToken(accessToken,refreshToken,ui);
                return;
            }
        }
        UserIdentity userIdentity = UserIdentity.builder()
                .sub(sub)
                .provider(provider)
                .user(user.get())
                .build();

        userIdentityRepository.save(userIdentity);

        providerTokenService.createNewToken(accessToken,refreshToken,userIdentity);

    }
    Optional<UserIdentity> findByProviderAndSub(String provider,String sub){
        return userIdentityRepository.findByProviderAndSub(provider,sub);
    }

    Optional<UserIdentity> findByUserAndProvider(User user,String provider){
        return userIdentityRepository.findByUserAndProvider(user.getId(),provider);
    }


}

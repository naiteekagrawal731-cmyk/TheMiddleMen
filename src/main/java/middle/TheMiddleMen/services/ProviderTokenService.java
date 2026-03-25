package middle.TheMiddleMen.services;

import middle.TheMiddleMen.entities.UserIdentity;
import middle.TheMiddleMen.entities.tokens.ProviderToken;
import middle.TheMiddleMen.repos.ProviderTokenRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProviderTokenService {

    private final ProviderTokenRepository providerTokenRepository;

    public ProviderTokenService(ProviderTokenRepository providerTokenRepository) {
        this.providerTokenRepository = providerTokenRepository;
    }


    public void createNewToken(OAuth2AccessToken accessToken, OAuth2RefreshToken refreshToken, UserIdentity userIdentity){
        Optional<ProviderToken> pt = providerTokenRepository.findByUserIdentity(userIdentity);
        if(pt.isPresent()){
            pt.get().setAccessToken(accessToken);
            pt.get().setRefreshToken(refreshToken);
            providerTokenRepository.save(pt.get());
            return;
        }
        providerTokenRepository.save(ProviderToken.builder()
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .userIdentity(userIdentity)
                .build());
    }

    Optional<ProviderToken> getByUserIdentity(UserIdentity userIdentity){
        return providerTokenRepository.findByUserIdentity(userIdentity);
    }
}

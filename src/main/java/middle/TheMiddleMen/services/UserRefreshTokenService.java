package middle.TheMiddleMen.services;

import com.nimbusds.oauth2.sdk.token.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import middle.TheMiddleMen.entities.User;
import middle.TheMiddleMen.entities.tokens.UserRefreshToken;
import middle.TheMiddleMen.repos.UserRefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserRefreshTokenService {

    private final UserRefreshTokenRepository userRefreshTokenRepository;

    public UserRefreshTokenService(UserRefreshTokenRepository userRefreshTokenRepository) {
        this.userRefreshTokenRepository = userRefreshTokenRepository;
    }
    @Transactional
    UUID generateRefreshToken(User user){
        Optional<UserRefreshToken> refreshToken = userRefreshTokenRepository.findByUser(user);
        UserRefreshToken token;
        if(refreshToken.isEmpty()){
            token = UserRefreshToken.builder()
                    .token(UUID.randomUUID())
                    .user(user)
                    .build();
        }else{
            token = refreshToken.get();
        }
        userRefreshTokenRepository.save(token);
        log.info("Refresh token for user = "+user.getUsername()+" created successfully");
        return token.getToken();
    }

    boolean isRefreshTokenValid(UUID refreshToken){
        Optional<UserRefreshToken> userRefreshToken =  userRefreshTokenRepository.findByToken(refreshToken);

        if(!userRefreshToken.isEmpty()){
            return userRefreshToken.get().getExpiryAt().isAfter(Instant.now());
        }
        return false;
    }

    User getUserByRefreshToken(UUID refreshToken){
        User user = userRefreshTokenRepository.findByToken(refreshToken).get().getUser();

        return user;
    }
}

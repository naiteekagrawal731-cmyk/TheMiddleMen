package middle.TheMiddleMen.services;

import lombok.extern.slf4j.Slf4j;
import middle.TheMiddleMen.clients.YoutubeClientV3;
import middle.TheMiddleMen.dtos.youtubeDtos.channelDtos.AllChannelInfo;
import middle.TheMiddleMen.dtos.youtubeDtos.playListDto.PlayListInfo;
import middle.TheMiddleMen.dtos.youtubeDtos.videoDto.VideoData;
import middle.TheMiddleMen.dtos.youtubeDtos.videoDto.VideosDetails;
import middle.TheMiddleMen.entities.User;
import middle.TheMiddleMen.entities.UserIdentity;
import middle.TheMiddleMen.entities.tokens.ProviderToken;
import middle.TheMiddleMen.exceptions.customExceptions.UsernameNotFound;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class YoutubeService {

    private final UserIdentityService userIdentityService;
    private final ProviderTokenService providerTokenService;
    private final YoutubeClientV3 youtubeClientV3;
    private final UserService userService;
    //Yotube access token provider is Google
    private final String provider = "google";

    public YoutubeService(UserIdentityService userIdentityService, ProviderTokenService providerTokenService, YoutubeClientV3 youtubeClientV3, UserService userService) {
        this.userIdentityService = userIdentityService;
        this.providerTokenService = providerTokenService;
        this.youtubeClientV3 = youtubeClientV3;
        this.userService = userService;
    }

    public Mono<AllChannelInfo> getAllChannelInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info("Getting provider access token");
        User user = userService.getUserByUsername(auth.getName()).orElseThrow(()-> new UsernameNotFound("User with username = "+auth.getName()+" does not exist"));


        UserIdentity ui = userIdentityService.findByUserAndProvider(user,provider).orElseThrow(() -> new RuntimeException("Invalid provider or user"));
        ProviderToken pt = providerTokenService.getByUserIdentity(ui).orElseThrow(() -> new RuntimeException("Provider token deos not exist"));
        log.info("Provider access token = "+pt.getAccessToken().getTokenValue());
        return youtubeClientV3.getAllChannelInfo(pt.getAccessToken());

    }
    public Mono<PlayListInfo> getUploadPlayListInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info("Getting provider access token");
        User user = userService.getUserByUsername(auth.getName()).orElseThrow(()-> new UsernameNotFound("User with username = "+auth.getName()+" does not exist"));


        UserIdentity ui = userIdentityService.findByUserAndProvider(user,provider).orElseThrow(() -> new RuntimeException("Invalid provider or user"));
        ProviderToken pt = providerTokenService.getByUserIdentity(ui).orElseThrow(() -> new RuntimeException("Provider token deos not exist"));
        log.info("Provider access token = "+pt.getAccessToken().getTokenValue());
        return youtubeClientV3.getUploadPlayListInfo(pt.getAccessToken());
    }

    public Mono<VideosDetails> getVideoDetails(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info("Getting provider access token");
        User user = userService.getUserByUsername(auth.getName()).orElseThrow(()-> new UsernameNotFound("User with username = "+auth.getName()+" does not exist"));


        UserIdentity ui = userIdentityService.findByUserAndProvider(user,provider).orElseThrow(() -> new RuntimeException("Invalid provider or user"));
        ProviderToken pt = providerTokenService.getByUserIdentity(ui).orElseThrow(() -> new RuntimeException("Provider token deos not exist"));
        log.info("Provider access token = "+pt.getAccessToken().getTokenValue());
        return youtubeClientV3.getVideoDetails(pt.getAccessToken());
    }
}

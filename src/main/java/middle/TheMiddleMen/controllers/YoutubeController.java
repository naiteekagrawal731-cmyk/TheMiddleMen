package middle.TheMiddleMen.controllers;

import lombok.extern.slf4j.Slf4j;
import middle.TheMiddleMen.dtos.youtubeDtos.channelDtos.AllChannelInfo;
import middle.TheMiddleMen.dtos.youtubeDtos.playListDto.PlayListInfo;
import middle.TheMiddleMen.dtos.youtubeDtos.videoDto.VideoData;
import middle.TheMiddleMen.dtos.youtubeDtos.videoDto.VideosDetails;
import middle.TheMiddleMen.services.YoutubeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/youtube")
@Slf4j
public class YoutubeController {

    private final YoutubeService youtubeService;

    public YoutubeController(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;
    }

    @GetMapping("/channel")
    public Mono<AllChannelInfo> getAllChannelInfo(){
        return youtubeService.getAllChannelInfo();
    }

    @GetMapping("/videos")
    public Mono<VideosDetails> getVideoDetails(){
        return youtubeService.getVideoDetails();
    }

    @GetMapping("/uploadPlaylist")
    public Mono<PlayListInfo> getPlayListInfo(){
        return youtubeService.getUploadPlayListInfo();
    }
}

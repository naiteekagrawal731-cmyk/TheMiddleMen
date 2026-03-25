package middle.TheMiddleMen.clients;

import lombok.extern.slf4j.Slf4j;
import middle.TheMiddleMen.dtos.youtubeDtos.channelDtos.AllChannelInfo;
import middle.TheMiddleMen.dtos.youtubeDtos.playListDto.PlayListContentDetails;
import middle.TheMiddleMen.dtos.youtubeDtos.playListDto.PlayListInfo;
import middle.TheMiddleMen.dtos.youtubeDtos.playListDto.PlayListIteams;
import middle.TheMiddleMen.dtos.youtubeDtos.videoDto.VideoData;
import middle.TheMiddleMen.dtos.youtubeDtos.videoDto.VideosDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class YoutubeClientV3 {

    private final WebClient webClient;

    public YoutubeClientV3(WebClient.Builder webClient) {
        this.webClient = webClient.baseUrl("https://www.googleapis.com/youtube/v3").build();
    }

    public Mono<AllChannelInfo> getAllChannelInfo(OAuth2AccessToken accessToken){
        log.info("Getting channel info");
        return webClient.get()
                .uri("/channels?part=snippet,statistics,contentDetails&mine=true&fields=items(id,snippet(title,description,thumbnails),statistics(subscriberCount,viewCount,videoCount,hiddenSubscriberCount),contentDetails.relatedPlaylists.uploads)")
                .headers(headers -> {
                    headers.setBearerAuth(accessToken.getTokenValue());
                })
                .retrieve()
                .bodyToMono(AllChannelInfo.class);
    }
    /*
        JSON Example
        {
          "kind": "youtube#channelListResponse",
          "items": [
            {
              "id": "UC_CHANNEL_ID",
              "snippet": {
                "title": "Sample Channel",
                "description": "Technology tutorials",
                "publishedAt": "2023-01-01T00:00:00Z"
              },
              "contentDetails": {
                "relatedPlaylists": {
                  "uploads": "UU_UPLOADS_PLAYLIST_ID"
                }
              },
              "statistics": {
                "viewCount": "100000",
                "subscriberCount": "5000",
                "videoCount": "120"
              }
            }
          ]
        }
         */
    public Mono<PlayListInfo> getUploadPlayListInfo(OAuth2AccessToken accessToken){
        log.info("Getting upload playlist info");
        return getAllChannelInfo(accessToken).flatMap(allChannelInfo -> {
            log.info("Upload playlist id = "+allChannelInfo.getChannels().get(0).getContentDetails().getRelatedPlaylists().getUploads());
             return webClient.get()
                    .uri("/playlistItems?part=snippet,contentDetails&playlistId="+allChannelInfo.getChannels().get(0).getContentDetails().getRelatedPlaylists().getUploads()+"&maxResults=10")
                     .headers(headers -> {
                        headers.setBearerAuth(accessToken.getTokenValue());
                    })
                    .retrieve()
                    .bodyToMono(PlayListInfo.class);

        });
    }
    /*
    {
  "kind": "youtube#playlistItemListResponse",
  "nextPageToken": "NEXT_PAGE_TOKEN",
  "items": [
    {
      "contentDetails": {
        "videoId": "VIDEO_ID_1"
      },
      "snippet": {
        "title": "Spring Boot Tutorial",
        "publishedAt": "2025-02-01T10:00:00Z",
        "thumbnails": {
          "high": {
            "url": "https://i.ytimg.com/vi/VIDEO_ID_1/hqdefault.jpg"
          }
        }
      }
    },
    {
      "contentDetails": {
        "videoId": "VIDEO_ID_2"
      },
      "snippet": {
        "title": "Dynamic Programming Explained",
        "publishedAt": "2025-02-05T12:00:00Z"
      }
    }
  ]
}
     */

    public Mono<VideosDetails> getVideoDetails(OAuth2AccessToken accessToken){
        log.info("Getting video info");
        StringBuilder requestURl = new StringBuilder("/videos?part=snippet,statistics,contentDetails&id=");
        return getUploadPlayListInfo(accessToken).flatMap(playListInfo -> {
            List<PlayListIteams> iteams = playListInfo.getItems();
            List<String> videoIds = iteams.stream().map(PlayListIteams::getContentDetails).map(PlayListContentDetails::getVideoId).toList();

            for(String id : videoIds){
                if(requestURl.charAt(requestURl.length()-1) != '='){
                    requestURl.append(',');
                }
                requestURl.append(id);
            }
            log.info("video ids = "+videoIds);
            return webClient.get()
                    .uri(new String(requestURl))
                    .headers(headers -> {
                        headers.setBearerAuth(accessToken.getTokenValue());
                    })
                    .retrieve()
                    .bodyToMono(VideosDetails.class)
                    .map(videosDetails -> {
                        //Filtering out ghost videos
                        log.info("Filtering out ghost videos");
                        List<VideoData> datas = videosDetails.getItems();
                        List<VideoData> filteredData = new ArrayList<>();
                        for(VideoData vd :datas){
                            if(Integer.valueOf(vd.getStatistics().getViewCount()) > 0){
                                //Currently being sorted on a video must have at least 1 view
                                filteredData.add(vd);
                            }
                        }
                        videosDetails.setItems(filteredData);
                        return videosDetails;
                    });

        });
    }





}

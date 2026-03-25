package middle.TheMiddleMen.clients;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class YoutubeClientV2 {

    private final WebClient webClient;

    public YoutubeClientV2(WebClient.Builder webClient) {
        this.webClient = webClient
                .baseUrl("https://www.googleapis.com/youtube/v2")
                .build();
    }
}

package middle.TheMiddleMen.dtos.youtubeDtos.channelDtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelSnippet {

    private String title;
    private String description;
    private String customUrl;
    private String publishedAt;

}

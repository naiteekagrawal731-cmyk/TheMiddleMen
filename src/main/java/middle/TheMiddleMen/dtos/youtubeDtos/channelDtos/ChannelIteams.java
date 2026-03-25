package middle.TheMiddleMen.dtos.youtubeDtos.channelDtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelIteams {

    private String channelId;

    private ChannelSnippet snippet;

    private ChannelStatistics statistics;

    private ContentDetails contentDetails;
}

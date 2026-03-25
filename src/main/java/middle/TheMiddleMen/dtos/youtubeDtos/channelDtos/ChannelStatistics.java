package middle.TheMiddleMen.dtos.youtubeDtos.channelDtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelStatistics {
    private long viewCount;
    private long subscriberCount;
    private long videoCount;
}

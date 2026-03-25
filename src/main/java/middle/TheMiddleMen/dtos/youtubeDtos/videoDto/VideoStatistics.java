package middle.TheMiddleMen.dtos.youtubeDtos.videoDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoStatistics {
    private String viewCount;
    private String likeCount;
    private String commentCount;
}

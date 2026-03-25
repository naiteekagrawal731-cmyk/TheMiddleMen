package middle.TheMiddleMen.dtos.youtubeDtos.videoDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoData {
    private String id;
    private VideoSnippet snippet;
    private VideoStatistics statistics;

}

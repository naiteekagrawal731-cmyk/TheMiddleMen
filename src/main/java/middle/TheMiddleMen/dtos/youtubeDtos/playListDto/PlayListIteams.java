package middle.TheMiddleMen.dtos.youtubeDtos.playListDto;

import lombok.*;
import middle.TheMiddleMen.dtos.youtubeDtos.videoDto.VideoSnippet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayListIteams {
    private PlayListContentDetails contentDetails;
    private VideoSnippet snippet;
}

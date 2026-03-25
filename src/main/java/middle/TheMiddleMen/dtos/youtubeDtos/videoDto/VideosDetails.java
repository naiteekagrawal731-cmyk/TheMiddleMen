package middle.TheMiddleMen.dtos.youtubeDtos.videoDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideosDetails {

    private List<VideoData> items;
}

package middle.TheMiddleMen.dtos.youtubeDtos.videoDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoThumbnails {

    private VideoHighThumbnail high;
    private VideoDefaultThumbnail defaultThumbnail;
    private VideoMediumThumbnail medium;
}

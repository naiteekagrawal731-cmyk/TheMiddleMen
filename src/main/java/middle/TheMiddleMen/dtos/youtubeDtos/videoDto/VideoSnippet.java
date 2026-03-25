package middle.TheMiddleMen.dtos.youtubeDtos.videoDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoSnippet {
    private String title;
    private String description;
    private String publishedAt;
    private VideoThumbnails thumbnails;
    private List<String> tags;

}

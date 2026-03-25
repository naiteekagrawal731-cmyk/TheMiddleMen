package middle.TheMiddleMen.dtos.youtubeDtos.playListDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayListInfo {

    private List<PlayListIteams> items;
}

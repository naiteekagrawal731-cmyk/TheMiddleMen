package middle.TheMiddleMen.dtos.youtubeDtos.channelDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllChannelInfo {

    @JsonProperty("items")
    List<ChannelIteams> channels;
}

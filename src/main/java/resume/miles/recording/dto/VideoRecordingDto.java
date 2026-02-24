package resume.miles.recording.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoRecordingDto {
    private Long id;
    private Long interviewLinkId;
    private String videoLink;
}

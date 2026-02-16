package resume.miles.moodEqualizer.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoodEqualizerDTO {
    private Long id;

    private Long awarenessId;

    
    private String url;

    private String name;
    private String type;

    // Defaulting to 1 to match the DB default, but allows override
    @Builder.Default
    private Integer status = 1;

    // These are typically read-only in a request, but useful for the response
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

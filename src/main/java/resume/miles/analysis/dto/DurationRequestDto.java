package resume.miles.analysis.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DurationRequestDto {
    private String token;
    private String duration;
}

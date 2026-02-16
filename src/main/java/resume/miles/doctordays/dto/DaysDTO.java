package resume.miles.doctordays.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DaysDTO {

    private Long id;

    private String dayName;
    private String shortName;
    private Integer status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

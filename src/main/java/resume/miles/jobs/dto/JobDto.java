package resume.miles.jobs.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {
    private Long id;

    private String jobId;

   private String clientName;

    private String role;

    private String jd;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

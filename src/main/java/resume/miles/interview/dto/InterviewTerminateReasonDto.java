package resume.miles.interview.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewTerminateReasonDto {

    @NotBlank(message="reason is required")
    private String reason;

    @NotBlank(message="token is required")
    private String token;
}

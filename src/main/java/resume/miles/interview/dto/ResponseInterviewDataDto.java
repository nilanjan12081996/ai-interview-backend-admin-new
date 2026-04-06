package resume.miles.interview.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseInterviewDataDto {
    private Integer coding;
    private Integer interviewChecking;
}

package resume.miles.questions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AnswerDTO {
    private Long id;
    private String answer;
    private Integer status;
    private Integer point;
    private Long questionId; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

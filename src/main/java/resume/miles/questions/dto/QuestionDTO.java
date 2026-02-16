package resume.miles.questions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class QuestionDTO {
    private Long id;
    private String question;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private List<AnswerDTO> answer;
}

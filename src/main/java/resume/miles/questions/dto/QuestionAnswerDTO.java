package resume.miles.questions.dto;

import java.time.LocalDateTime;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class QuestionAnswerDTO {

    private Long id;

    @NotBlank(message="question is required")
    private String question;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<AnswerDTO> answer;
}

package resume.miles.questionAwarenessmap.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.questions.dto.QuestionDTO;
import resume.miles.questions.entity.Question;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwarenessQuestionMapDTO {

    private Long id;

    @NotNull(message = "Question ID cannot be null")
    @JsonProperty("question_id") 
    private Long questionId;

    @NotNull(message = "Awareness ID cannot be null")
    @JsonProperty("awareness_id") 
    private Long awarenessId;

    @Builder.Default
    private Integer status = 1;

   
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    private QuestionDTO question;
}

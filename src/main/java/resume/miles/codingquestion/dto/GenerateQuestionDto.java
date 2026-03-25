package resume.miles.codingquestion.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenerateQuestionDto {

    @NotBlank(message = "Token is required")
    private String token;
}

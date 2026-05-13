package resume.miles.codingans.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodingAnsDto {
    private Long id;

    @NotBlank(message="token required")
    private String token;

    @NotNull(message="question required")
    private Long questionId;

    @NotNull(message="ans required")
    private String ans;

    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

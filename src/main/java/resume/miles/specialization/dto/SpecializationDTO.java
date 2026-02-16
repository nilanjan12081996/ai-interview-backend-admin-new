package resume.miles.specialization.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecializationDTO {

    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    private String des;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
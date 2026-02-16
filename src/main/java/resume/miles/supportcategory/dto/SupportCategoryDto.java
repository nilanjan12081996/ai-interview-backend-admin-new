package resume.miles.supportcategory.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportCategoryDto {

    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    private String description;

    
    private String image;

    @NotNull(message = "parentId cannot be empty")
    private Long parentId;

    @NotNull(message = "status cannot be empty")
    private Integer status;

   
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

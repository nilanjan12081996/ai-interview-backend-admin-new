package resume.miles.awarness.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAwarenessDTO {
    
    private Long id;

    private String awarenessName;

    
    private String image;

   
    private String description;

    
    private String colorCode;

    
    private Long subsidebarId;

    private Integer status;

  
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

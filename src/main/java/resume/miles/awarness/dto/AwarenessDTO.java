package resume.miles.awarness.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwarenessDTO {

    private Long id;

    @NotBlank(message = "Awareness name is required")
    private String awarenessName;

    
    private String image;

    @NotBlank(message = "Description is required")
    private String description;

    
    private String colorCode;

    @NotNull(message = "subsidebarId is required")
    private Long subsidebarId;

    private Integer status;

  
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @AssertTrue(message = "Color code is required when subsidebarId is 3")
    public boolean isColorCodeValid() {
        if (subsidebarId != null && subsidebarId == 3) {
            return colorCode != null && !colorCode.trim().isEmpty();
        }
        return true;
    }
    @AssertTrue(message = "This api is restricted for PsychiatricInsights")
    public boolean isApiNotapplicable() {
       if (subsidebarId != null && subsidebarId == 4) {
        return false; // Validation FAILS if id is 4
       }
        return true;
    }
}

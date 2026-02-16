package resume.miles.awarness.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.AssertTrue;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAwarenessDTO {

    @NotNull(message = "Id is required")
    private Long id;
    
    private String awarenessName;

    private String description;

    
    private String colorCode;

    @NotNull(message = "subsidebarId is required")
    private Long subsidebarId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @AssertTrue(message = "Color code is required when subsidebarId is 3")
    public boolean isColorCodeValid() {
        if (subsidebarId != null && subsidebarId == 3) {
            return colorCode != null && !colorCode.trim().isEmpty();
        }
        return true;
    }
}

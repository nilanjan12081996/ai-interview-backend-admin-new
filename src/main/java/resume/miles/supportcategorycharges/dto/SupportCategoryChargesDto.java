package resume.miles.supportcategorycharges.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportCategoryChargesDto {
    private Long id;
    private Long support_category;
    private Integer charge;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
}

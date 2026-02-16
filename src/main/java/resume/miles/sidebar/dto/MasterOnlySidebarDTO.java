package resume.miles.sidebar.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterOnlySidebarDTO {
    private Long id;
    private String sidebarName;
    private String sidebarShortName;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

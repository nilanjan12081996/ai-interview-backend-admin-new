package resume.miles.sidebar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterSidebarDto {

    private Long id;
    private String sidebarName;
    private String sidebarShortName;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<SubSidebarDto> subsidebar ;
   
}

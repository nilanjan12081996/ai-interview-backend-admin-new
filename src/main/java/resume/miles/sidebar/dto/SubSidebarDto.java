package resume.miles.sidebar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubSidebarDto {

    private Long id;
    private String subSidebarName;
    private String subSidebarShortName;


    private Long masterSidebarId; 

    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private MasterSidebarDto masterSidebarDto;
}
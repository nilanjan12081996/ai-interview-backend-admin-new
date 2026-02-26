package resume.miles.musthaveskill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.jobs.dto.JobDto;
import resume.miles.jobs.entity.JobEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MustHaveSkillDto {
     private Long id;

    private Long jobId;   // instead of full JobEntity

    private String skillName;

    private Integer status;


    private JobDto jobDetails;
}

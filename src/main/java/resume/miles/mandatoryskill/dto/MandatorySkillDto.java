package resume.miles.mandatoryskill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.jobs.dto.JobDto;
import resume.miles.jobs.entity.JobEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MandatorySkillDto {

    private Long id;

    private Long jobId;   // instead of full JobEntity

    private String skillName;

    private Integer status;

    private JobDto jobDetails;
}
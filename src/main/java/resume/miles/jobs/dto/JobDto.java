package resume.miles.jobs.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import resume.miles.mandatoryskill.dto.MandatorySkillDto;
import resume.miles.mandatoryskill.entity.MandatorySkillEntity;
import resume.miles.musthaveskill.dto.MustHaveSkillDto;
import resume.miles.musthaveskill.entity.MustHaveSkillEntity;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {
    private Long id;

    private String jobId;

   private String clientName;

    private String role;

    private String jd;
    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private String experience;
 
    private List<MustHaveSkillDto> mustHaveSkills;
    private List<MandatorySkillDto> mandatorySkills;
}

package resume.miles.musthaveskill.mapper;

import resume.miles.jobs.entity.JobEntity;
import resume.miles.mandatoryskill.dto.MandatorySkillDto;
import resume.miles.mandatoryskill.entity.MandatorySkillEntity;
import resume.miles.musthaveskill.dto.MustHaveSkillDto;
import resume.miles.musthaveskill.entity.MustHaveSkillEntity;


public class MustHaveSkillMapper {
     public static MustHaveSkillDto toDTO(MustHaveSkillEntity entity) {
    return MustHaveSkillDto.builder()
            .id(entity.getId())
            .jobId(entity.getId())
            .skillName(entity.getSkillName())
            .status(entity.getStatus())
            .build();
}
public static MustHaveSkillEntity toEntity(MustHaveSkillDto dto) {
    MustHaveSkillEntity mustHaveSkillEntity = MustHaveSkillEntity.builder()
           
            .job(dto.getJobId())
            .skillName(dto.getSkillName())
            .status(dto.getStatus())
            .build();
        mustHaveSkillEntity.setId(dto.getId());

        return mustHaveSkillEntity;
}
    
}

package resume.miles.mandatoryskill.mapper;

import resume.miles.jobs.entity.JobEntity;
import resume.miles.mandatoryskill.dto.MandatorySkillDto;
import resume.miles.mandatoryskill.entity.MandatorySkillEntity;

public class MandatorySkillMapper {
    public static MandatorySkillDto toDTO(MandatorySkillEntity entity) {
    return MandatorySkillDto.builder()
            .id(entity.getId())
            .jobId(entity.getId())
            .skillName(entity.getSkillName())
            .status(entity.getStatus())
            .build();
}
public static MandatorySkillEntity toEntity(MandatorySkillDto dto) {
    MandatorySkillEntity mandatorySkillEntity = MandatorySkillEntity.builder()
           
            .job(dto.getJobId())
            .skillName(dto.getSkillName())
            .status(dto.getStatus())
            .build();
        mandatorySkillEntity.setId(dto.getId());

        return mandatorySkillEntity;
}
}

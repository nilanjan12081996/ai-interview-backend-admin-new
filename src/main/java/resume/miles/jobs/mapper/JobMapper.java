package resume.miles.jobs.mapper;

import java.util.Collections;

import org.springframework.stereotype.Component;
import resume.miles.client.entity.ClientEntity;
import resume.miles.jobs.dto.JobDto;
import resume.miles.jobs.entity.JobEntity;
import resume.miles.mandatoryskill.mapper.MandatorySkillMapper;
import resume.miles.musthaveskill.mapper.MustHaveSkillMapper;

@Component
public class JobMapper {

    // 🔥 Entity → DTO
    public JobDto toDto(JobEntity entity) {

        if (entity == null) {
            return null;
        }

        return JobDto.builder()
                .id(entity.getId())
                .jobId(entity.getJobId())
                .clientName(
                        entity.getClient() != null
                                ? entity.getClient().getClientName()
                                : null
                )
                .role(entity.getRole())
                .mandatorySkills(entity.getMandatorySkills()!=null?entity.getMandatorySkills().stream().map(MandatorySkillMapper::toDTO).toList():Collections.emptyList())
                .mustHaveSkills(entity.getMustHaveSkills() != null ?entity.getMustHaveSkills().stream().map(MustHaveSkillMapper::toDTO).toList():Collections.emptyList())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .experience(entity.getExperience())
                .jd(entity.getJd())
                .level(entity.getLevel())
                .build();
    }


    public static JobDto toDtoData(JobEntity entity) {

        if (entity == null) {
            return null;
        }

        return JobDto.builder()
                .id(entity.getId())
                .jobId(entity.getJobId())
                .clientName(
                        entity.getClient() != null
                                ? entity.getClient().getClientName()
                                : null
                )
                .role(entity.getRole())
                .mandatorySkills(entity.getMandatorySkills()!=null?entity.getMandatorySkills().stream().map(MandatorySkillMapper::toDTO).toList():Collections.emptyList())
                .mustHaveSkills(entity.getMustHaveSkills() != null ?entity.getMustHaveSkills().stream().map(MustHaveSkillMapper::toDTO).toList():Collections.emptyList())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .experience(entity.getExperience())
                .jd(entity.getJd())
                .level(entity.getLevel())
                .build();
    }

    // 🔥 DTO → Entity
    // ClientEntity must be passed from service
    public JobEntity toEntity(JobDto dto, ClientEntity client) {

        if (dto == null) {
            return null;
        }

        return JobEntity.builder()
                .jobId(dto.getJobId())
                .client(client)
                .role(dto.getRole())
                .status(dto.getStatus())
                .jd(dto.getJd())
                .experience(dto.getExperience())
                .level(dto.getLevel())
                .build();
    }


}

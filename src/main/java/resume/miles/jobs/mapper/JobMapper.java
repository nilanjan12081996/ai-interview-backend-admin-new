package resume.miles.jobs.mapper;

import org.springframework.stereotype.Component;
import resume.miles.client.entity.ClientEntity;
import resume.miles.jobs.dto.JobDto;
import resume.miles.jobs.entity.JobEntity;

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
                .jd(entity.getJd())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
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
                .jd(dto.getJd())
                .build();
    }
}

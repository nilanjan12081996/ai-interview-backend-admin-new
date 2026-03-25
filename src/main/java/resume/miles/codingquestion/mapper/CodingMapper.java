package resume.miles.codingquestion.mapper;


import resume.miles.codingquestion.dto.CodingDTO;
import resume.miles.codingquestion.entity.CodingEntity;

public class CodingMapper {


    private CodingMapper() {
    }

    public static CodingDTO toDto(CodingEntity entity) {
        if (entity == null) {
            return null;
        }

        return CodingDTO.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .questionData(entity.getQuestionData())
                .status(entity.getStatus())
                .isDeleted(entity.getIsDeleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static CodingEntity toEntity(CodingDTO dto) {
        if (dto == null) {
            return null;
        }

        return CodingEntity.builder()
                .id(dto.getId())
                .token(dto.getToken())
                .questionData(dto.getQuestionData())
                // If status or isDeleted are null in DTO, default them to 1 and 0
                .status(dto.getStatus() != null ? dto.getStatus() : 1)
                .isDeleted(dto.getIsDeleted() != null ? dto.getIsDeleted() : 0)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

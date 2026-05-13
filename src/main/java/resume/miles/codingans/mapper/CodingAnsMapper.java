package resume.miles.codingans.mapper;

import resume.miles.codingans.dto.CodingAnsDto;
import resume.miles.codingans.entity.CodingAnsEntity;

public class CodingAnsMapper {

    // Private constructor to prevent instantiation of utility class
    private CodingAnsMapper() {
    }

    public static CodingAnsDto toDto(CodingAnsEntity entity) {
        if (entity == null) {
            return null;
        }

        return CodingAnsDto.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .questionId(entity.getQuestionId())
                .ans(entity.getAns())
                .status(entity.getStatus())
                .isDeleted(entity.getIsDeleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static CodingAnsEntity toEntity(CodingAnsDto dto) {
        if (dto == null) {
            return null;
        }

        return CodingAnsEntity.builder()
                .id(dto.getId())
                .token(dto.getToken())
                .questionId(dto.getQuestionId())
                .ans(dto.getAns())
                // Ensure defaults are respected if null is passed in the DTO
                .status(dto.getStatus() != null ? dto.getStatus() : 1)
                .isDeleted(dto.getIsDeleted() != null ? dto.getIsDeleted() : 0)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

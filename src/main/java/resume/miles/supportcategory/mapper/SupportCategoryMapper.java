package resume.miles.supportcategory.mapper;

import resume.miles.supportcategory.dto.SupportCategoryDto;
import resume.miles.supportcategory.entity.SupportCategoryEntity;

public class SupportCategoryMapper {

  
    private SupportCategoryMapper() {}

    public static SupportCategoryEntity toEntity(SupportCategoryDto dto) {
        if (dto == null) {
            return null;
        }

        SupportCategoryEntity entity = SupportCategoryEntity.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .image(dto.getImage())
            .parentId(dto.getParentId())
            .status(dto.getStatus())
            .build();

  
            entity.setId(dto.getId());

            return entity;
    }

    public static SupportCategoryDto toDto(SupportCategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return SupportCategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .image(entity.getImage())
                .parentId(entity.getParentId())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

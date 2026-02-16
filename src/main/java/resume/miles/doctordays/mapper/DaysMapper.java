package resume.miles.doctordays.mapper;

import resume.miles.doctordays.dto.DaysDTO;

import resume.miles.doctordays.entity.DaysEntity;

public class DaysMapper {

    public static DaysDTO toDto(DaysEntity entity) {
        if (entity == null) return null;

        return DaysDTO.builder()
                .id(entity.getId())
                .dayName(entity.getDay_name())
                .shortName(entity.getShort_name())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static DaysEntity toEntity(DaysDTO dto) {
        if (dto == null) return null;

        return DaysEntity.builder()
                .day_name(dto.getDayName())
                .short_name(dto.getShortName())
                .status(dto.getStatus())
                .build();
    }
}

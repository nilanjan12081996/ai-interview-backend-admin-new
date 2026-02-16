package resume.miles.specialization.mapper;

import resume.miles.specialization.dto.SpecializationDTO;
import resume.miles.specialization.entity.SpecializationEntity;

public class SpecializationMapper {

   
    private SpecializationMapper() {}

    public static SpecializationDTO toDTO(SpecializationEntity entity) {
        if (entity == null) {
            return null;
        }

        SpecializationDTO dto = new SpecializationDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDes(entity.getDes());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        return dto;
    }

    public static SpecializationEntity toEntity(SpecializationDTO dto) {
        if (dto == null) {
            return null;
        }

        SpecializationEntity entity = new SpecializationEntity();
      
        entity.setId(dto.getId()); 
        entity.setName(dto.getName());
        entity.setDes(dto.getDes());
        
        
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        
      
        
        return entity;
    }
}
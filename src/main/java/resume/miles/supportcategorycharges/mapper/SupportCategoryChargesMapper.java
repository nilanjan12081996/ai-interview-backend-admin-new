package resume.miles.supportcategorycharges.mapper;

import org.springframework.stereotype.Component;

import resume.miles.supportcategorycharges.dto.SupportCategoryChargesDto;
import resume.miles.supportcategorycharges.entity.SupportCategoryChargesEntity;

@Component
public class SupportCategoryChargesMapper {
    public SupportCategoryChargesMapper(){

    }

    public SupportCategoryChargesDto toDto(SupportCategoryChargesEntity supportcatEntity){
        return SupportCategoryChargesDto.builder()
        .id(supportcatEntity.getId())
        .support_category(supportcatEntity.getSupportCategory())
        .charge(supportcatEntity.getCharge())
        .status(supportcatEntity.getStatus())
        .createdAt(supportcatEntity.getCreatedAt())
        .updatedAt(supportcatEntity.getUpdatedAt())
        .build();

    }
    public SupportCategoryChargesEntity toEntity(SupportCategoryChargesDto dto){
        System.out.println("support_category"+dto.getSupport_category());
        return SupportCategoryChargesEntity.builder()
        .charge(dto.getCharge())
        .status(dto.getStatus())
        .supportCategory(dto.getSupport_category())
        .build();
    }
    
}

package resume.miles.awarness.mapper;

import org.springframework.beans.factory.annotation.Value;

import resume.miles.awarness.dto.AwarenessDTO;
import resume.miles.awarness.dto.ResponseAwarenessDTO;
import resume.miles.awarness.entiry.AwarenessEntity;

public class AwarnessMapper {

    public static AwarenessDTO toAwarenessDTO(AwarenessEntity entity) {
            if (entity == null) {
                return null;
            }

            return AwarenessDTO.builder()
                    .id(entity.getId())
                    .awarenessName(entity.getAwarenessName())
                    .image(entity.getImage())
                    .description(entity.getDescription())
                    .colorCode(entity.getColorCode())
                    .subsidebarId(entity.getSubsidebarId())
                    .status(entity.getStatus())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build();
    }
    public static AwarenessEntity toEntityAwareness(AwarenessDTO entity){
        if (entity == null) {
                return null;
        }
        return AwarenessEntity.builder()
                .id(entity.getId())
                .awarenessName(entity.getAwarenessName())
                .image(entity.getImage())
                .description(entity.getDescription())
                .colorCode(entity.getColorCode())
                .subsidebarId(entity.getSubsidebarId())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
     public static ResponseAwarenessDTO toResponseAwareness(AwarenessEntity entity,String baseUrl){
        if (entity == null) {
                return null;
        }
       
        return ResponseAwarenessDTO.builder()
                .id(entity.getId())
                .awarenessName(entity.getAwarenessName())
                .image(entity.getImage() != null ? baseUrl+entity.getImage():null)
                .description(entity.getDescription())
                .colorCode(entity.getColorCode())
                .subsidebarId(entity.getSubsidebarId())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

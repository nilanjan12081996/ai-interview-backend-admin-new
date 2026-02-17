package resume.miles.client.mapper;

import resume.miles.client.dto.ClientDto;
import resume.miles.client.entity.ClientEntity;

public class ClientMapper {
    public static ClientEntity toEntity(ClientDto dto) {

        if (dto == null) {
            return null;
        }

        return ClientEntity.builder()
                .clientName(dto.getClientName())
                .status(dto.getStatus())
                .build();
    }
      public static ClientDto toDto(ClientEntity entity) {

        if (entity == null) {
            return null;
        }

        return ClientDto.builder()
                .id(entity.getId())
                .clientName(entity.getClientName())
                .status(entity.getStatus())
                .build();
    }
}

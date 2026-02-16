package resume.miles.users.mapper;

import resume.miles.users.dto.UserDto;
import resume.miles.users.entity.UserEntity;

public class UserMapper {
    
     // DTO → Entity
    public static UserEntity toEntity(UserDto dto) {

        if (dto == null) {
            return null;
        }

        return UserEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }


    public static UserDto toDto(UserEntity entity) {

        if (entity == null) {
            return null;
        }

        return UserDto.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .build(); 
    }
}

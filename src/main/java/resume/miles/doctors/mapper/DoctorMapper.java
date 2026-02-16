package resume.miles.doctors.mapper;

import resume.miles.doctors.dto.DoctorDTO;
import resume.miles.doctors.entity.DoctorEntity;

public class DoctorMapper {

    private DoctorMapper() {
        // utility class
    }

    // ================= Entity → DTO =================
    public static DoctorDTO toDto(DoctorEntity entity) {
        if (entity == null) {
            return null;
        }

        return DoctorDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .mobile(entity.getMobile())
                .avatar(entity.getAvatar())
                .otp(entity.getOtp())
                .otpExpire(entity.getOtpExpire())
                .oAuth(entity.getOAuth())
                .oauthProvider(entity.getOauthProvider())
                .status(entity.getStatus())
                .adminStatus(entity.getAdminStatus())
                .isDeleted(entity.getIsDeleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    // ================= DTO → Entity =================
    public static DoctorEntity toEntity(DoctorDTO dto) {
        if (dto == null) {
            return null;
        }

        return DoctorEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .mobile(dto.getMobile())
                .avatar(dto.getAvatar())
                .otp(dto.getOtp())
                .otpExpire(dto.getOtpExpire())
                .oAuth(dto.getOAuth())
                .oauthProvider(dto.getOauthProvider())
                .status(dto.getStatus())
                .adminStatus(dto.getAdminStatus())
                .isDeleted(dto.getIsDeleted())
                .build();
    }
}

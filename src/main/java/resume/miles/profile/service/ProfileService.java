package resume.miles.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import resume.miles.profile.dto.ProfileDto;
import resume.miles.superadmin.dto.SuperAdminResponseDTO;
import resume.miles.superadmin.entity.SuperAdmin;
import resume.miles.superadmin.repository.SuperadminRepository;
import resume.miles.users.dto.UserDto;
import resume.miles.users.entity.UserEntity;
import resume.miles.users.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final SuperadminRepository superadminRepository;
    private final UserRepository userRepository;

    @Value("${urls.baseUrl}")
    private String baseUrl;

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    public SuperAdminResponseDTO profile(Long userId) {
        Optional<SuperAdmin> superAdmin =  superadminRepository.findById(userId);
        if(superAdmin.isEmpty()){
                throw new RuntimeException("id not found"+ userId);
        }
        return SuperAdminResponseDTO.builder()
                .id(superAdmin.get().getId())
                .email(superAdmin.get().getEmail())
                .avatar(baseUrl+"/"+superAdmin.get().getAvatar())
                .firstName(superAdmin.get().getFirstName())
                .lastName(superAdmin.get().getLastName())
                .username(superAdmin.get().getUsername())
                .mobile(superAdmin.get().getMobile())
                .build();
    }
    public UserDto profileHr(Long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new RuntimeException("id not found"+ userId);
        }
        return UserDto.builder()
                .id(user.get().getId())
                .email(user.get().getEmail())
                .lastName(user.get().getLastName())
                .firstName(user.get().getFirstName())
                .username(user.get().getUsername())
                .companyName(user.get().getCompanyName())
                .mobile(user.get().getMobile())
                .gender(user.get().getGender())
                .avatar(baseUrl+"/"+user.get().getAvatar())
                .build();
    }

    @Transactional
    public String updateProfile(Long userId, ProfileDto profileDto) {

        SuperAdmin admin = superadminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("SuperAdmin ID not found: " + userId));


        admin.setFirstName(profileDto.getFristName());
        admin.setLastName(profileDto.getLastName());
        admin.setMobile(profileDto.getMobile());


        return "Success";
    }

    @Transactional
    public String updateProfileHr(Long userId, ProfileDto profileDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setFirstName(profileDto.getFristName());
        user.setLastName(profileDto.getLastName());

        user.setGender(profileDto.getGender());

        userRepository.save(user);

        return "Success";
    }


    @Transactional
    public String uploadAvatar(Long userId, MultipartFile file) throws IOException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = "avatar_" + userId + ".jpg";
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


        String publicUrl = "/public/" + fileName;
        user.setAvatar(publicUrl);
        userRepository.save(user);

        return publicUrl;
    }
    @Transactional
    public String uploadAvatarAdmin(Long userId, MultipartFile file) throws IOException {
        SuperAdmin user = superadminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = "avatar_admin_" + userId + ".jpg";
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


        String publicUrl = "/public/" + fileName;
        user.setAvatar(publicUrl);
        superadminRepository.save(user);

        return publicUrl;
    }
}

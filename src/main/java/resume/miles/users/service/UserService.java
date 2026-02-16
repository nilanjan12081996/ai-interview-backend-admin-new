package resume.miles.users.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import resume.miles.users.dto.UserDto;
import resume.miles.users.entity.UserEntity;
import resume.miles.users.mapper.UserMapper;
import resume.miles.users.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
 private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

     public UserDto createUser(UserDto userDto) {

        // 1️⃣ Validate password match
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new RuntimeException("Password and Confirm Password do not match");
        }

        // 2️⃣ Check duplicate email
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // 3️⃣ Check duplicate username
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // 4️⃣ Convert DTO → Entity
        UserEntity userEntity = UserMapper.toEntity(userDto);

        // 5️⃣ Encode password
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userEntity.setIsDeleted(0);
        userEntity.setStatus(1);

        // 6️⃣ Save user
        UserEntity savedUser = userRepository.save(userEntity);

        // 7️⃣ Convert Entity → DTO
        return UserMapper.toDto(savedUser);
    }

       public List<UserDto> getAllUsers() {

        return userRepository.findByIsDeleted(0)
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }


        // ✅ GET USER BY ID
    public UserDto getUserById(Long id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.toDto(user);
    }


      public void deleteUser(Long id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsDeleted(1);

        userRepository.save(user);
    }
    
}

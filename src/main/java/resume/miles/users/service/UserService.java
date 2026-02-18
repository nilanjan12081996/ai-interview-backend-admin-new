package resume.miles.users.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import resume.miles.config.JwtUtil;
import resume.miles.users.dto.LoginRequestDto;
import resume.miles.users.dto.UserDto;
import resume.miles.users.entity.UserEntity;
import resume.miles.users.mapper.UserMapper;
import resume.miles.users.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
 private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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


 public void toggleUserStatus(Long id) {

    UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setStatus(user.getStatus() == 1 ? 0 : 1);

    userRepository.save(user);
}


  public Map<String, Object> loginUser(LoginRequestDto loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (user.getIsDeleted() == 1) throw new RuntimeException("Account not found");
        if (user.getStatus() == 0) throw new RuntimeException("Account is disabled");

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateUserToken(user);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", UserMapper.toDto(user));
        return result;
    }


    
}

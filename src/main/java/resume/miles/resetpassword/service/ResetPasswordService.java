package resume.miles.resetpassword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resume.miles.resetpassword.dto.PasswordResetDto;
import resume.miles.users.entity.UserEntity;
import resume.miles.users.repository.UserRepository;
import resume.miles.users.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

        private final PasswordEncoder passwordEncoder;
        private final UserRepository userRepository;

        @Transactional
        public String ResetPassword(PasswordResetDto dto,Long id) {
            Optional<UserEntity> user = userRepository.findById(id);
            if(user.isEmpty()) {
                throw new RuntimeException("User not found");
            }
            if (user.get().getAdminPasswordCount() != null && user.get().getAdminPasswordCount() >= 2) {
                throw new RuntimeException("You are not allowed to reset your password");
            }
            String password = user.get().getPassword();
            System.out.println(dto.getOldPassword()+"matches");
            boolean matches = passwordEncoder.matches(dto.getOldPassword(),password);
            System.out.println(matches+"matches");
            if(!matches) {
                throw new RuntimeException("Wrong old Password");
            }
            String newPassword = passwordEncoder.encode(dto.getNewPassword());
            Integer currentCount = user.get().getAdminPasswordCount();

            int nextCount = (currentCount == null) ? 1 : currentCount + 1;

            user.get().setAdminPasswordCount(nextCount);
            user.get().setPassword(newPassword);

            return "updated";
        }
}

package resume.miles.superadmin.serviceImpl.login;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import resume.miles.config.JwtUtil;
import resume.miles.superadmin.dto.SuperAdminLoginDTO;
import resume.miles.superadmin.dto.SuperAdminOtp;
import resume.miles.superadmin.dto.SuperAdminResponseDTO;
import resume.miles.superadmin.entity.SuperAdmin;
import resume.miles.superadmin.repository.SuperadminRepository;
import resume.miles.superadmin.service.login.SuperAdminLoginService;
import resume.miles.superadmin.serviceImpl.login.email.SuperAdminEmailServiceOtpImpl;

// @Service
// public class SuperAdminLoginServiceImpl implements SuperAdminLoginService {
//     @Autowired
//     private SuperadminRepository superadminRepository;

//     @Autowired
//     private PasswordEncoder passwordEncoder;

//     @Autowired
//     @Qualifier("loginTaskExecutor")
//     private ThreadPoolTaskExecutor taskExecutor;

//     @Autowired
//     private SuperAdminEmailServiceOtpImpl superAdminEmailServiceOtpImpl;

//     @Override
//     public SuperAdminOtp loginservice(SuperAdminLoginDTO suparAdminLoginDTO) {
//         // Validation & OTP generation (main thread)
//         SuperAdmin superAdmin = superadminRepository.findByEmailOrUsername(
//             suparAdminLoginDTO.getUsernameOrEmail(), 
//             suparAdminLoginDTO.getUsernameOrEmail()
//         ).orElseThrow(() -> new RuntimeException("Email or Username invalid"));
        
//         if (superAdmin.getStatus() != 1) {
//             throw new RuntimeException("Account is inactive");
//         }
        
//         if (!passwordEncoder.matches(suparAdminLoginDTO.getPassword(), superAdmin.getPassword())) {
//             throw new RuntimeException("Invalid Password");
//         }
        
//         // Generate OTP and save to DB
//         Random random = new Random();
//         Integer otp = 100000 + random.nextInt(900000);
//         LocalDateTime now = LocalDateTime.now();
//         LocalDateTime expiry = now.plus(5, ChronoUnit.MINUTES);
        
//         superAdmin.setOtp(otp);
//         superAdmin.setOtpExpire(expiry); 
        
        
//         superadminRepository.save(superAdmin);
        
    
//         SuperAdminOtp response = new SuperAdminOtp();
//         response.setId(superAdmin.getId());
//         response.setOtp(otp);
      
        
 
//         CompletableFuture<Void> emailFuture = CompletableFuture.runAsync(() -> {
//             try {
//                 superAdminEmailServiceOtpImpl.sendOtpEmail(superAdmin.getEmail(), otp);
                
//             } catch (Exception e) {

//             }
//         }, taskExecutor);
        
//         // Return response immediately, email runs in background
//         // return emailFuture.thenApply(v -> response);
//         return response;
//     }
// }


@Service
public class SuperAdminLoginServiceImpl implements SuperAdminLoginService {

    @Autowired
    private SuperadminRepository superadminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
public String loginservice(SuperAdminLoginDTO superAdminLoginDTO) {

    SuperAdmin superAdmin = superadminRepository
            .findByEmailOrUsername(
                    superAdminLoginDTO.getUsernameOrEmail(),
                    superAdminLoginDTO.getUsernameOrEmail()
            )
            .orElseThrow(() -> new RuntimeException("Email or Username invalid"));

    if (superAdmin.getStatus() != 1) {
        throw new RuntimeException("Account is inactive");
    }

    if (!passwordEncoder.matches(
            superAdminLoginDTO.getPassword(),
            superAdmin.getPassword())) {
        throw new RuntimeException("Invalid Password");
    }

    // 🔥 Convert Entity → DTO
    SuperAdminResponseDTO responseDTO = SuperAdminResponseDTO.builder()
            .id(superAdmin.getId())
            .username(superAdmin.getUsername())
            .email(superAdmin.getEmail())
            .firstName(superAdmin.getFirstName())
            .lastName(superAdmin.getLastName())
            .mobile(superAdmin.getMobile())
            .status(superAdmin.getStatus())
            .tokenType("SUPER_ADMIN") // or whatever role you use
            .build();

    // Generate token
    return jwtUtil.generateSuperAdminToken(responseDTO);
}

  
}


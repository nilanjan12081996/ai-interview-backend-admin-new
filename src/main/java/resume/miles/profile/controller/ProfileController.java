package resume.miles.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import resume.miles.config.JwtUserDetails;
import resume.miles.profile.dto.ProfileDto;
import resume.miles.profile.service.ProfileService;
import resume.miles.superadmin.dto.SuperAdminLoginDTO;
import resume.miles.superadmin.dto.SuperAdminResponseDTO;
import resume.miles.users.dto.UserDto;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/get")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal JwtUserDetails userDetails) {
        Map<String,Object> response = new HashMap<>();
        try{
            Long userId = userDetails.getId();

            SuperAdminResponseDTO superAdminResponseDTO =  profileService.profile(userId);

            response.put("status",true);
            response.put("message","success");
            response.put("data",superAdminResponseDTO);
            response.put("statusCode",200);

            return ResponseEntity.status(200).body(response);
        }catch(Exception ex){
            response.put("status",false);
            response.put("message",ex.getMessage());
            response.put("statusCode",400);

            return ResponseEntity.status(400).body(response);
        }
    }
    @GetMapping("/get/hr")
    public ResponseEntity<?> getProfileHr(@AuthenticationPrincipal JwtUserDetails userDetails) {
        Map<String,Object> response = new HashMap<>();
        try{
            Long userId = userDetails.getId();

            UserDto userDto =  profileService.profileHr(userId);

            response.put("status",true);
            response.put("message","success");
            response.put("data",userDto);
            response.put("statusCode",200);

            return ResponseEntity.status(200).body(response);
        }catch(Exception ex){
            response.put("status",false);
            response.put("message",ex.getMessage());
            response.put("statusCode",400);

            return ResponseEntity.status(400).body(response);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> setSuperAdminProfile(@RequestBody ProfileDto profileDto, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Map<String,Object> response = new HashMap<>();
        try{
            Long userId = userDetails.getId();

           String retrun =  profileService.updateProfile(userId,profileDto);

            response.put("status",true);
            response.put("message","retrun");
            response.put("statusCode",200);

            return ResponseEntity.status(200).body(response);
        }catch(Exception ex){
            response.put("status",false);
            response.put("message",ex.getMessage());
            response.put("statusCode",400);

            return ResponseEntity.status(400).body(response);
        }
    }

    @PatchMapping("/update/hr")
    public ResponseEntity<?> setUserProfile(@RequestBody ProfileDto profileDto, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Map<String,Object> response = new HashMap<>();
        try{
            Long userId = userDetails.getId();

            String retrun =  profileService.updateProfileHr(userId,profileDto);

            response.put("status",true);
            response.put("message","retrun");
            response.put("statusCode",200);

            return ResponseEntity.status(200).body(response);
        }catch(Exception ex){
            response.put("status",false);
            response.put("message",ex.getMessage());
            response.put("statusCode",400);

            return ResponseEntity.status(400).body(response);
        }
    }

    @PatchMapping(value = "/update-avatar/hr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> setAvatar(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = userDetails.getId();

            // Pass the file to the service
            String avatarPath = profileService.uploadAvatar(userId, file);

            response.put("status", true);
            response.put("message", "Avatar updated successfully");
            response.put("avatarUrl", avatarPath);
            response.put("statusCode", 200);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex.getMessage());
            response.put("statusCode", 400);
            return ResponseEntity.status(400).body(response);
        }
    }

    @PatchMapping(value = "/update-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> setAdminAvatar(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = userDetails.getId();

            // Pass the file to the service
            String avatarPath = profileService.uploadAvatarAdmin(userId, file);

            response.put("status", true);
            response.put("message", "Avatar updated successfully");
            response.put("avatarUrl", avatarPath);
            response.put("statusCode", 200);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", ex.getMessage());
            response.put("statusCode", 400);
            return ResponseEntity.status(400).body(response);
        }
    }
}

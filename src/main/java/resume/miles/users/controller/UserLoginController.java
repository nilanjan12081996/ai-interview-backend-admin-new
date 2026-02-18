package resume.miles.users.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import resume.miles.users.dto.LoginRequestDto;
import resume.miles.users.dto.UserDto;
import resume.miles.users.service.UserService;

@RestController
@RequestMapping("/api/goodmood/hr")
@RequiredArgsConstructor
public class UserLoginController {
    private final UserService userService;
    

@PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequest) {
    try {
        Map<String, Object> data = userService.loginUser(loginRequest);
        return ResponseEntity.ok(Map.of(
            "message", "Login successful",
            "data", data,
            "status", true,
            "statusCode", 200,
            "role","USER"
        ));
    } catch (Exception e) {
        return ResponseEntity.status(401).body(Map.of(
            "message", e.getMessage(),
            "status", false,
            "status_code", 401
        ));
    }
}

}

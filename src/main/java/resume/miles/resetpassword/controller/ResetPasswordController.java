package resume.miles.resetpassword.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resume.miles.resetpassword.dto.PasswordResetDto;
import resume.miles.resetpassword.service.ResetPasswordService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/goodmood/reset")
@RequiredArgsConstructor
public class ResetPasswordController {


    private final ResetPasswordService resetPasswordService;

    @PostMapping("/password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDto dto, BindingResult bindingResult   ) {
        Map<String,Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                    errors.put(error.getObjectName(), error.getDefaultMessage());
            }
            response.put("status",false);
            response.put("message","validation error");
            response.put("statusCode",422);
            response.put("errors",errors);
            return  ResponseEntity.status(422).body(response);
        }
        try{
            response.put("status",true);
            response.put("message","Reset Password Successfully");
            response.put("statusCode",200);
            return  ResponseEntity.status(200).body(response);
        }catch (Exception e){
            response.put("status",false);
            response.put("message",e.getMessage());
            response.put("statusCode",400);
            return  ResponseEntity.status(400).body(response);
        }
    }
}

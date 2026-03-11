package resume.miles.interview.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import resume.miles.interview.dto.InterviewTerminateReasonDto;
import resume.miles.interview.service.InterviewTerminateService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goodmood/terminate")
public class InterviewTerminateController {

    private final InterviewTerminateService interviewTerminateService;


    @PostMapping("/reason")
    public ResponseEntity<?> interviewTerminate(@Valid @RequestBody InterviewTerminateReasonDto interviewTerminateReasonDto, BindingResult bindingResult) {
        Map<String,Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            response.put("message","validation failure");
            response.put("status",false);
            response.put("statusCode",422);
            response.put("errors",errors);
            return ResponseEntity.status(422).body(response);
        }
        try{
            String data = interviewTerminateService.updateTerminateReason(interviewTerminateReasonDto);
            response.put("message","update successfully");
            response.put("status",true);
            response.put("statusCode",200);
            return ResponseEntity.status(200).body(response);
        }catch (Exception e){
            response.put("message",e.getMessage());
            response.put("status",false);
            response.put("statusCode",400);
            return ResponseEntity.status(400).body(response);
        }
    }


    @PostMapping("/user/reason")
    public ResponseEntity<?> interviewReasonUser(@Valid @RequestBody InterviewTerminateReasonDto interviewTerminateReasonDto, BindingResult bindingResult) {
        Map<String,Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            response.put("message","validation failure");
            response.put("status",false);
            response.put("statusCode",422);
            response.put("errors",errors);
            return ResponseEntity.status(422).body(response);
        }
        try{
            String data = interviewTerminateService.updateTerminateReasonUser(interviewTerminateReasonDto);
            response.put("message","update successfully");
            response.put("status",true);
            response.put("statusCode",200);
            return ResponseEntity.status(200).body(response);
        }catch (Exception e){
            response.put("message",e.getMessage());
            response.put("status",false);
            response.put("statusCode",400);
            return ResponseEntity.status(400).body(response);
        }
    }
}

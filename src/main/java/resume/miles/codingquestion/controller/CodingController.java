package resume.miles.codingquestion.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resume.miles.codingquestion.dto.GenerateQuestionDto;
import resume.miles.codingquestion.service.CodingService;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/coding")
@RequiredArgsConstructor
@RestController
public class CodingController {

    private final CodingService codingService;

    @PostMapping("/generate")
    public ResponseEntity<?> codingRequest(@Valid  @RequestBody GenerateQuestionDto generateQuestionDto, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        if(bindingResult.hasErrors()){
            Map<String,String> errors = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.put("statusCode", 422);
            response.put("message","validation error");
            response.put("status", false);
            response.put("error", errors);
            return ResponseEntity.status(422).body(response);
        }
        try{

            String data = codingService.generateCode(generateQuestionDto.getToken());
            response.put("statusCode", 200);
            response.put("data", data);
            response.put("message","question generated");
            response.put("status", true);

            return ResponseEntity.status(200).body(response);
        }catch(Exception e){
            response.put("error", e.getStackTrace());
            response.put("statusCode", 400);
            response.put("message", e.getMessage());
            response.put("status", false);

            return ResponseEntity.status(400).body(response);
        }

    }



}

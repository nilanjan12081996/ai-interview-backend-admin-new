package resume.miles.codingans.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import resume.miles.codingans.dto.CodingAnsDto;
import resume.miles.codingans.service.CodingAnsService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aiinterview/coding/ans")
public class CodingAnsController {

    private final CodingAnsService codingAnsService;

    @PostMapping("/save")
    public ResponseEntity<?> saveCodingAns(@RequestBody CodingAnsDto codingAnsDto, BindingResult bindingResult) {
        Map<String,Object> response = new HashMap<>();
        try{
            Map<String,String> errors = new HashMap<>();
            if(bindingResult.hasErrors()){
                for(FieldError fieldError : bindingResult.getFieldErrors()){
                    errors.put(fieldError.getField(),fieldError.getDefaultMessage());
                }
                response.put("statusCode", 422);
                response.put("message", "validation error");
                response.put("errors", errors);
                response.put("status", false);

                return ResponseEntity.status(422).body(response);
            }
            CodingAnsDto codingAnsDtoResponse = codingAnsService.save(codingAnsDto);
            response.put("statusCode", 200);
            response.put("message", "ans save to db");
            response.put("data", codingAnsDtoResponse);
            response.put("status", true);

            return ResponseEntity.status(200).body(response);
        }catch(RuntimeException e){
            response.put("statusCode", 422);
            response.put("message", e.getMessage());
            response.put("status", false);

            return ResponseEntity.status(422).body(response);
        }catch(Exception e){
            response.put("statusCode", 400);
            response.put("message", e.getMessage());
            response.put("status", false);

            return ResponseEntity.status(400).body(response);
        }
    }


    @GetMapping("/list/{id}")
    public ResponseEntity<?> getCodeList(@PathVariable Long id) {
        Map<String,Object> response = new HashMap<>();
        try{
            CodingAnsDto codingAnsDtoResponse = codingAnsService.list(id);
            response.put("statusCode", 200);
            response.put("message", "ans found");
            response.put("status", true);

            return ResponseEntity.status(200).body(response);
        }catch(RuntimeException e){
            response.put("statusCode", 422);
            response.put("message", e.getMessage());
            response.put("status", false);

            return ResponseEntity.status(422).body(response);
        }catch(Exception e){
            response.put("statusCode", 400);
            response.put("message", e.getMessage());
            response.put("status", false);

            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping("/code/{token}")
    public ResponseEntity<?> getCodeWithToken(@PathVariable String token) {
        Map<String,Object> response = new HashMap<>();
        try{
            CodingAnsDto codingAnsDtoResponse = codingAnsService.listWithToken(token);
            response.put("statusCode", 200);
            response.put("message", "ans found");
            response.put("ans", codingAnsDtoResponse);
            response.put("status", true);

            return ResponseEntity.status(200).body(response);
        }catch(RuntimeException e){
            response.put("statusCode", 422);
            response.put("message", e.getMessage());
            response.put("status", false);

            return ResponseEntity.status(422).body(response);
        }catch(Exception e){
            response.put("statusCode", 400);
            response.put("message", e.getMessage());
            response.put("status", false);

            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping("/code-ans/{token}")
    public ResponseEntity<?> getCodeWithToken(@PathVariable String token) {
        Map<String,Object> response = new HashMap<>();
        try{
            CodingAnsDto codingAnsDtoResponse = codingAnsService.listWithToken(token);
            response.put("statusCode", 200);
            response.put("message", "ans found");
            response.put("ans", codingAnsDtoResponse);
            response.put("status", true);

            return ResponseEntity.status(200).body(response);
        }catch(RuntimeException e){
            response.put("statusCode", 422);
            response.put("message", e.getMessage());
            response.put("status", false);

            return ResponseEntity.status(422).body(response);
        }catch(Exception e){
            response.put("statusCode", 400);
            response.put("message", e.getMessage());
            response.put("status", false);

            return ResponseEntity.status(400).body(response);
        }
    }
}

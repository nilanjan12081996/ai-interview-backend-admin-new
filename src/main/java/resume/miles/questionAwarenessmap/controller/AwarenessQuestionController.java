package resume.miles.questionAwarenessmap.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.BindingResult;

import resume.miles.questionAwarenessmap.dto.AwarenessQuestionMapDTO;


import resume.miles.questionAwarenessmap.service.AwarenessQuestionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/goodmood/awareness/question")
public class AwarenessQuestionController {
    @Autowired
    private AwarenessQuestionService awarenessQuestionService;

    @PostMapping("/mapped")
    public ResponseEntity<?> mapped(@Validated @RequestBody AwarenessQuestionMapDTO entity,BindingResult bindingResult) {
         if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
        
            return ResponseEntity.status(422).body(Map.of(
                "message", "Validation failed",
                "status", false,
                "statusCode", 422,
                "errors", errors
            ));
        }
        try{
            boolean isMapped = awarenessQuestionService.mapped(entity);
            return ResponseEntity.status(200).body(Map.of(
                "message", " Add succefully",
                "status", true,
                "statusCode", 200
            ));
        }catch(RuntimeException e){
            return ResponseEntity.status(200).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
            ));
        }catch(Exception e){
            return ResponseEntity.status(200).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400,
                "errors", e.getStackTrace()
            ));
        }
    }

     @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try{
            boolean isMapped = awarenessQuestionService.delete(id);
            return ResponseEntity.status(200).body(Map.of(
                "message", " Delete succefully",
                "status", true,
                "statusCode", 200
            ));
        }catch(RuntimeException e){
            return ResponseEntity.status(200).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
            ));
        }catch(Exception e){
            return ResponseEntity.status(200).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400,
                "errors", e.getStackTrace()
            ));
        }
    }


    @PatchMapping("/status/{id}")
    public ResponseEntity<?> status(@PathVariable Long id) {
        try{
            boolean isMapped = awarenessQuestionService.status(id);
            return ResponseEntity.status(200).body(Map.of(
                "message", " status update succefully",
                "status", true,
                "statusCode", 200
            ));
        }catch(RuntimeException e){
            return ResponseEntity.status(200).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
            ));
        }catch(Exception e){
            return ResponseEntity.status(200).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400,
                "errors", e.getStackTrace()
            ));
        }
    }


    @GetMapping("/awarness-question/{id}")
    public ResponseEntity<?> data(@PathVariable Long id) {
        try{
             List<AwarenessQuestionMapDTO>  data = awarenessQuestionService.getQuestions(id);
            return ResponseEntity.status(200).body(Map.of(
                "message", " status update succefully",
                "status", true,
                "statusCode", 200,
                "data",data
            ));
        }catch(RuntimeException e){
            return ResponseEntity.status(200).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
            ));
        }catch(Exception e){
            return ResponseEntity.status(200).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400,
                "errors", e.getStackTrace()
            ));
        }
    }
    
}

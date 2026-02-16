package resume.miles.questions.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Positive;
import resume.miles.questions.dto.QuestionAnswerDTO;
import resume.miles.questions.dto.QuestionDTO;
import resume.miles.questions.entity.Question;
import resume.miles.questions.serviceImpl.QuestionAnswerServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/goodmood/question/answer")
public class QuestionAnsController {

    @Autowired
    private QuestionAnswerServiceImpl questionAnswerServiceImpl;

    @PostMapping("/add-edit")
    public ResponseEntity<?> addEditQuestionAns(@Validated @RequestBody QuestionAnswerDTO questionAnswerDTO,BindingResult bindingResult) {
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
            QuestionAnswerDTO data = questionAnswerServiceImpl.insertQuestionAns(questionAnswerDTO);
            return ResponseEntity.status(200).body(Map.of(
                "message", "Question ans add succefully",
                "status", true,
                "statusCode", 200,
                "data",data
            ));
        }catch(RuntimeException e){
            return ResponseEntity.status(422).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 422
                
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400,
                "errors", e.getStackTrace()
            ));
        }
    }
    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(required = false) Long id) {
        try{
             List<QuestionDTO>  question = questionAnswerServiceImpl.list(id);
             return ResponseEntity.status(200).body(Map.of(
                "message", "Question ans add succefully",
                "status", true,
                "statusCode", 200,
                "data",question
            ));
        }catch(RuntimeException e){
            return ResponseEntity.status(422).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 422
                
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400,
                "errors", e.getStackTrace()
            ));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable 
            @Positive(message = "ID must be a positive number") 
            Long id) {
        try{
            questionAnswerServiceImpl.delete(id);
              return ResponseEntity.status(200).body(Map.of(
                "message", "Ans delete succefully",
                "status", true,
                "statusCode", 200
            ));
        }catch(RuntimeException e){
            return ResponseEntity.status(422).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 422
                
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400,
                "errors", e.getStackTrace()
            ));
        }
    }
    @PutMapping("/status-ans/{id}")
    public ResponseEntity<?> ans(@PathVariable 
            @Positive(message = "ID must be a positive number") 
            Long id) {
        try{
            questionAnswerServiceImpl.ansStatus(id);
              return ResponseEntity.status(200).body(Map.of(
                "message", "status change succefully",
                "status", true,
                "statusCode", 200
            ));
        }catch(RuntimeException e){
            return ResponseEntity.status(422).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 422
                
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400,
                "errors", e.getStackTrace()
            ));
        }
    }

    @PatchMapping("/status-question/{id}")
    public ResponseEntity<?> question(@PathVariable 
            @Positive(message = "ID must be a positive number") 
            Long id) {
        try{
            questionAnswerServiceImpl.questionStatus(id);
              return ResponseEntity.status(200).body(Map.of(
                "message", "status change succefully",
                "status", true,
                "statusCode", 200
            ));
        }catch(RuntimeException e){
            return ResponseEntity.status(422).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 422
                
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400,
                "errors", e.getStackTrace()
            ));
        }
    }
}

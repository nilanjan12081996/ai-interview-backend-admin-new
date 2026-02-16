package resume.miles.blog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import resume.miles.blog.dto.PostDTO;
import resume.miles.blog.service.PostService;
import resume.miles.config.JwtUserDetails;


@RestController
@RequestMapping("/api/goodmood/post")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/save")
    public ResponseEntity<?> postSave(@Validated @RequestBody PostDTO postDTO,BindingResult bindingResult,@AuthenticationPrincipal JwtUserDetails userDetails) {
       try{
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
            Long userId = userDetails.getId();
            String fullname = userDetails.getFirstname() +" "+ userDetails.getLastname();
            System.out.println(userId+"name");
            PostDTO data = postService.saveAndEdit(postDTO,userId,fullname);
            return ResponseEntity.status(201).body(Map.of(
                "message","Post Added",
                "status", true,
                "statusCode", 201,
               "data",data
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
    @PatchMapping("/publish/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@AuthenticationPrincipal JwtUserDetails userDetails) {
        try{
            boolean status = postService.publish(id);
            return ResponseEntity.status(200).body(Map.of(
                "message","Post published",
                "status", true,
                "statusCode", 200
            
            ));
        }catch(RuntimeException e){
             return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
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


    @PatchMapping("/image/{id}")
    public ResponseEntity<?> image(@RequestParam("file") MultipartFile file ,@PathVariable Long id,@AuthenticationPrincipal JwtUserDetails userDetails) {
        try{
            boolean status = postService.uploadImage(id,file);
            return ResponseEntity.status(200).body(Map.of(
                "message","Post image upload",
                "status", true,
                "statusCode", 200
            
            ));
        }catch(RuntimeException e){
             return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
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
    public ResponseEntity<?> list(@RequestParam(required = false) Long id,@RequestParam(required = false) String slug,@AuthenticationPrincipal JwtUserDetails userDetails) {
        try{
            List<PostDTO> data = postService.list(id,slug);
            return ResponseEntity.status(200).body(Map.of(
                "message","Post image upload",
                "status", true,
                "statusCode", 200,
                "data",data
            
            ));
        }catch(RuntimeException e){
             return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
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

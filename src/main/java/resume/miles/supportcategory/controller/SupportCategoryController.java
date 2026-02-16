package resume.miles.supportcategory.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import resume.miles.supportcategory.dto.SupportCategoryDto;
import resume.miles.supportcategory.service.SubcategoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/goodmood/support/category")
public class SupportCategoryController {
    @Autowired
    private SubcategoryService subcategoryService;

    @Value("${urls.baseUrl}")
    private String baseUrl;

    @PostMapping("/add/update")
    public ResponseEntity<?> add(@Valid @RequestBody SupportCategoryDto entity,BindingResult bindingResult) {
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
            SupportCategoryDto data =  subcategoryService.saveOrUpdate(entity);
            return ResponseEntity.status(200).body(Map.of(
                "message", "status change succefully",
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


    @GetMapping("/list/parents")
    public ResponseEntity<?> parent(@RequestParam(required = false) Long id) {
        try{
            List<SupportCategoryDto> data =  subcategoryService.list(id);
            return ResponseEntity.status(200).body(Map.of(
                "message", "status change succefully",
                "status", true,
                "statusCode", 200,
                "data",data,
                "baseurl",baseUrl
               
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

    @GetMapping("/list/child")
    public ResponseEntity<?> child(@RequestParam(required = true) Long pid ,@RequestParam(required = false) Long id) {
        try{
            List<SupportCategoryDto> data =  subcategoryService.childList(pid,id);
            return ResponseEntity.status(200).body(Map.of(
                "message", "status change succefully",
                "status", true,
                "statusCode", 200,
                "data",data,
                "baseurl",baseUrl
               
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


    @PatchMapping("/status/{id}")
    public ResponseEntity<?> status(@PathVariable(required = true) Long id) {
        try{
            SupportCategoryDto data =  subcategoryService.status(id);
            return ResponseEntity.status(200).body(Map.of(
                "message", "status change succefully",
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



    @PatchMapping("/image/{id}")
    public ResponseEntity<?> image(@RequestParam("file") MultipartFile file, @PathVariable(required = true) Long id) {
        try{
            SupportCategoryDto data =  subcategoryService.file(file,id);
            return ResponseEntity.status(200).body(Map.of(
                "message", "status change succefully",
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
    
    @GetMapping("/without-parent")
      public ResponseEntity<?> withoutParent() {
        try{
            List<SupportCategoryDto> data =  subcategoryService.getSubcategoryWithoutParent();
            return ResponseEntity.status(200).body(Map.of(
                "message", "status change succefully",
                "status", true,
                "statusCode", 200,
                "data",data,
                "baseurl",baseUrl
              
               
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

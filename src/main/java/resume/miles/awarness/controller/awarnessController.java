package resume.miles.awarness.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import resume.miles.awarness.dto.AwarenessDTO;
import resume.miles.awarness.dto.ResponseAwarenessDTO;
import resume.miles.awarness.dto.UpdateAwarenessDTO;
import resume.miles.awarness.service.AwarnessService;


@RestController
@RequestMapping("/api/goodmood/awarness")
public class awarnessController {

    @Autowired
    private AwarnessService awarnessService;

    @PostMapping("/save")
    public ResponseEntity<?> saveAwarness(@Validated @RequestBody AwarenessDTO awarnessDto, BindingResult bindingResult){
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
            AwarenessDTO awarenessDTO = awarnessService.saveData(awarnessDto);
            return ResponseEntity.status(201).body(Map.of(
                "message", " add succefully",
                "status", true,
                "statusCode", 201,
                "data",awarenessDTO
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

    @PatchMapping("/update")
    public ResponseEntity<?> update(@Validated @RequestBody UpdateAwarenessDTO awarnessDto, BindingResult bindingResult){
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
            AwarenessDTO awarenessDTO = awarnessService.updateData(awarnessDto);
            return ResponseEntity.status(201).body(Map.of(
                "message", "Update succefully",
                "status", true,
                "statusCode", 200,
                "data",awarenessDTO
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

    @PatchMapping("/image-upload/{id}") 
    public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file,@PathVariable("id") Long id){
        try{
            boolean res = awarnessService.uploadImage(id, file);
            return ResponseEntity.status(201).body(Map.of(
                "message", "Update succefully",
                "status", true,
                "statusCode", 200
               
            ));
        }catch(IOException e){
             return ResponseEntity.status(422).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 422
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

    @GetMapping("/list/by/subbar")
    public ResponseEntity<?> list(@RequestParam(required = true) Long id){
        
        try{
            List<ResponseAwarenessDTO> data = awarnessService.findDataWithSidebarId(id);
            return ResponseEntity.status(201).body(Map.of(
                "message", "List Found",
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



    @GetMapping("/single")
    public ResponseEntity<?> singleData(@RequestParam(required = true) Long id){
        
        try{
            ResponseAwarenessDTO data = awarnessService.findData(id);
            return ResponseEntity.status(201).body(Map.of(
                "message", "List Found",
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

    @PatchMapping("/status/{id}")
    public ResponseEntity<?> toggleStatus(@PathVariable("id") Long id){
        try {
            AwarenessDTO data=awarnessService.toggleStatus(id);
            return ResponseEntity.status(200).body(Map.of(
                "message", "Status toggled successfully",
                "status", true,
                "statusCode", 200
                
            ));
        } catch (RuntimeException e) {
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

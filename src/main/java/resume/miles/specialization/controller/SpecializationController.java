package resume.miles.specialization.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import resume.miles.specialization.dto.SpecializationDTO;
import resume.miles.specialization.service.SpecializationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goodmood/specialization")
public class SpecializationController {

    private final SpecializationService service;

    public SpecializationController(SpecializationService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SpecializationDTO dto) {
        try {
            SpecializationDTO data = service.create(dto);
            return ResponseEntity.status(200).body(Map.of(
                "message", "Specialization created successfully",
                "status", true,
                "statusCode", 200,
                "data", data
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(422).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 422
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
            ));
        }
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<?> update(@RequestBody SpecializationDTO dto) {
        try {
            if (dto.getId() == null) throw new RuntimeException("ID is required for update");
            
            SpecializationDTO data = service.update(dto);
            return ResponseEntity.status(200).body(Map.of(
                "message", "Specialization updated successfully",
                "status", true,
                "statusCode", 200,
                "data", data
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(422).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 422
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
            ));
        }
    }

    // TOGGLE STATUS (No status param required)
    @PatchMapping("/status/{id}")
    public ResponseEntity<?> toggleStatus(@PathVariable(required = true) Long id) {
        try {
            SpecializationDTO data = service.toggleStatus(id);
            return ResponseEntity.status(200).body(Map.of(
                "message", "Status changed successfully",
                "status", true,
                "statusCode", 200,
                "data", data
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(422).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 422
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400,
                "errors", e.getStackTrace()
            ));
        }
    }

    // GET (List or Single)
    @GetMapping
    public ResponseEntity<?> get(@RequestParam(required = false) Long id) {
        try {
            List<SpecializationDTO> data = service.getSpecializations(id);
            return ResponseEntity.status(200).body(Map.of(
                "message", "Data fetched successfully",
                "status", true,
                "statusCode", 200,
                "data", data
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
            ));
        }
    }
}
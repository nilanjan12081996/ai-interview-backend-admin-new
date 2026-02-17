package resume.miles.jobs.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import resume.miles.jobs.dto.JobDto;
import resume.miles.jobs.service.JobService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/goodmood/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // 🔥 CREATE JOB
    @PostMapping("/create-job")
    public ResponseEntity<?> createJob(
            @Valid @RequestBody JobDto dto,
            BindingResult bindingResult) {

        try {

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

                return ResponseEntity.status(422).body(Map.of(
                        "message", "Validation failed",
                        "status", false,
                        "statusCode", 422,
                        "errors", errors
                ));
            }

            JobDto saved = jobService.createJob(dto);

            return ResponseEntity.status(201).body(Map.of(
                    "message", "Job created successfully",
                    "status", true,
                    "statusCode", 201,
                    "data", saved
            ));

        } catch (RuntimeException e) {

            return ResponseEntity.status(422).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 422
            ));

        } catch (Exception e) {

            return ResponseEntity.status(400).body(Map.of(
                    "message", "Something went wrong",
                    "status", false,
                    "statusCode", 400
            ));
        }
    }

    // 🔥 LIST JOBS
    @GetMapping("/list-jobs")
    public ResponseEntity<?> listJobs() {

        try {

            return ResponseEntity.ok(Map.of(
                    "message", "Job list fetched successfully",
                    "status", true,
                    "statusCode", 200,
                    "data", jobService.listJobs()
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

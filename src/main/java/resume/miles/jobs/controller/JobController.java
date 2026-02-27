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


     @GetMapping("/filter-with-token")
    public ResponseEntity<?> listJobsToken() {

        try {

            return ResponseEntity.ok(Map.of(
                    "message", "Job list fetched successfully",
                    "status", true,
                    "statusCode", 200,
                    "data", jobService.listJobsToken()
            ));

        } catch (Exception e) {

            return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 400
            ));
        }
    }



@PatchMapping("/status/{id}")
public ResponseEntity<?> toggleStatus(@PathVariable Long id) {

    try {

        JobDto updatedJob = jobService.toggleJobStatus(id);

        return ResponseEntity.ok(Map.of(
                "message", "Status toggled successfully",
                "status", true,
                "statusCode", 200,
                "data", updatedJob
        ));

    } catch (Exception e) {

        return ResponseEntity.badRequest().body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
        ));
    }
}

@GetMapping("/{id}")
public ResponseEntity<?> getJobById(@PathVariable Long id) {

    try {

        JobDto job = jobService.getJobById(id);

        return ResponseEntity.ok(Map.of(
                "message", "Job fetched successfully",
                "status", true,
                "statusCode", 200,
                "data", job
        ));

    } catch (Exception e) {

        return ResponseEntity.badRequest().body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
        ));
    }
}


@PutMapping("/update-job/{id}")
public ResponseEntity<?> updateJob(
        @PathVariable Long id,
        @RequestBody JobDto dto) {

    try {

        JobDto updatedJob = jobService.updateJob(id, dto);

        return ResponseEntity.ok(Map.of(
                "message", "Job updated successfully",
                "status", true,
                "statusCode", 200,
                "data", updatedJob
        ));

    } catch (Exception e) {

        return ResponseEntity.badRequest().body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
        ));
    }
}


@DeleteMapping("/delete-job/{id}")
public ResponseEntity<?> deleteJob(@PathVariable Long id) {

    try {

        jobService.deleteJob(id);

        return ResponseEntity.ok(Map.of(
                "message", "Job deleted successfully",
                "status", true,
                "statusCode", 200
        ));

    } catch (Exception e) {

        return ResponseEntity.badRequest().body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
        ));
    }
}


}

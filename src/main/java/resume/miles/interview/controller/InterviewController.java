package resume.miles.interview.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import resume.miles.interview.dto.*;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.service.InterviewService;

@RestController
@RequestMapping("/api/goodmood/interview")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @GetMapping("/get/token/interview/type")
    public ResponseEntity<?>  interviewdetails(@RequestParam(required = true) String token){
        Map<String,Object> response = new HashMap<>();
        try{
                ResponseInterviewDataDto data = interviewService.dataDetails(token);

                response.put("status",true);
                response.put("satusCode",200);
                response.put("message","data found");
                response.put("data",data);

                return ResponseEntity.status(200).body(response);
        }catch(Exception e){
                response.put("status",false);
                response.put("statusCode",400);
                response.put("message",e.getMessage());
                response.put("error",e.getStackTrace());

                return ResponseEntity.status(400).body(response);
        }

    }

    @PostMapping(value = "/schedule", consumes = "multipart/form-data")
    public ResponseEntity<?> scheduleInterview(

           @ModelAttribute InterviewScheduleDto interviewScheduleDto,@RequestParam Integer coding,
           @RequestParam Integer interview
    ) {

        try {

            // Basic validation
            if (interviewScheduleDto.getResumeFile().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Resume file is required");
            }

            Map<String, String> result = interviewService.scheduleInterview(
                    interviewScheduleDto.getJobId(),
                    interviewScheduleDto.getCandidateName(),
                    interviewScheduleDto.getEmail(),
                    interviewScheduleDto.getPhoneNumber(),
                    interviewScheduleDto.getResumeFile(),
                    interviewScheduleDto.getIsCoding(),
                    interviewScheduleDto.getStartTime(),
                    interviewScheduleDto.getEndTime(),
                    interviewScheduleDto.getInterviewDate(),
                    coding,
                    interview
            );

            return ResponseEntity.status(201).body(Map.of(
                "message","Interview Scheduled Successfully.",
                "link",result.get("link"),
                "token",result.get("token"),
                "statusCode",201,
                "status",true
            ));

        } catch (IOException e) {

            return ResponseEntity
                    .status(400)
                    .body(Map.of(
                        "message","File Upload Faild"+e.getMessage(),
                        "status",false,
                        "statusCode",400
                    ));

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(400)
                    .body(Map.of(
                        "message","Invalid Input"+e.getMessage(),
                        "status",false,
                        "statusCode",400
                    ));

        } catch (Exception e) {

            return ResponseEntity
                    .status(400)
                    .body(Map.of(
                        "message",e.getMessage(),
                        "error",e.getStackTrace(),
                        "status",false,
                        "statusCode",400
                    ));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllInterviewSchedules() {

        try {

            List<InterviewScheduleResponseDto> response =
                    interviewService.getAllInterviewSchedules();

            return ResponseEntity.status(200).body(Map.of(
                "message","List fetch successfully",
                "status",true,
                "statusCode",200,
                "data",response
            ));

        } catch (Exception e) {

            return ResponseEntity
                    .status(400)
                    .body(Map.of(
                        "message",e.getMessage(),
                        "status",false,
                        "statusCode",400,
                        "error",e.getStackTrace()
                    ));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteInterviewSchedule(@RequestParam Long interviewScheduleId) {
        Map<String,Object> response = new HashMap<>();
        try{
            String message = interviewService.deleteCandidate(interviewScheduleId);
            response.put("status",true);
            response.put("statusCode",200);
            response.put("message",message);

            return ResponseEntity.status(200).body(response);
        }catch(RuntimeException e){
            response.put("status",false);
            response.put("statusCode",422);
            response.put("message",e.getMessage());

            return ResponseEntity.status(422).body(response);
        }catch(Exception e){
            response.put("status",false);
            response.put("statusCode",400);
            response.put("message",e.getMessage());
            response.put("error",e.getStackTrace());

            return ResponseEntity.status(400).body(response);
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> editInterviewSchedule(
            @PathVariable("id") Long interviewScheduleId,
            @Valid @RequestBody InterviewDto interviewDto) {

        Map<String, Object> response = new HashMap<>();
        try {
            // Pass both the ID and the new data to the service
            String message = interviewService.updateInterviewSchedule(interviewScheduleId, interviewDto);

            response.put("status", true);
            response.put("statusCode", 200);
            response.put("message", message);

            return ResponseEntity.status(200).body(response);

        } catch (RuntimeException e) {
            response.put("status", false);
            response.put("statusCode", 422);
            response.put("message", e.getMessage());

            return ResponseEntity.status(422).body(response);
        } catch (Exception e) {
            response.put("status", false);
            response.put("statusCode", 400);
            response.put("message", e.getMessage());
            // It's generally a security risk to expose full stack traces in production APIs,
            // but keeping it here as you had it for debugging.
            response.put("error", e.getStackTrace());

            return ResponseEntity.status(400).body(response);
        }
    }
     @GetMapping("/list-data-exp")
    public ResponseEntity<?> getExpAndAllSkill(@RequestParam(required = true) String token) {

        try {

             
            InterviewLinkDto interviewLinkDto = interviewService.getAllList(token);
            return ResponseEntity.status(200).body(Map.of(
                "message","List fetch successfully",
                "status",true,
                "statusCode",200,
                "data",interviewLinkDto
              
            ));

        } catch (Exception e) {

            return ResponseEntity
                    .status(400)
                    .body(Map.of(
                        "message",e.getMessage(),
                        "status",false,
                        "statusCode",400
                    ));
        }
    }

    @GetMapping("/job-role")
    public ResponseEntity<?> getJobRoleByToken(@RequestParam(required = true) String token) {
        try {
            String jobRole = interviewService.getJobRoleByToken(token);
            return ResponseEntity.status(200).body(Map.of(
                "message", "Job role fetched successfully",
                "status", true,
                "statusCode", 200,
                "data", Map.of("role", jobRole)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message", e.getMessage(),
                "status", false,
                "statusCode", 400
            ));
        }
    }

@GetMapping("/job/candidates/{jobId}")
public ResponseEntity<?> getCandidatesByJobId(
        @PathVariable Long jobId) {

    try {

        // List<InterviewDto> candidates =
        //         interviewService.getCandidatesByJobPrimaryId(jobId);

        List<InterviewScheduleResponseDto> candidates =
        interviewService.getCandidatesByJobPrimaryId(jobId);

        return ResponseEntity.ok(Map.of(
                "message", "Candidates fetched successfully",
                "status", true,
                "statusCode",200,
                "data", candidates
                
        ));

    } catch (Exception e) {

        return ResponseEntity.badRequest().body(Map.of(
                "message", e.getMessage(),
                "status", false
        ));
    }
}
    @PostMapping("/resend-link/{id}/{coding}/{interviewData}")
    public ResponseEntity<?> resendLink(@PathVariable("id") Long interviewId,@PathVariable("coding") Integer coding,@PathVariable("interviewData") Integer interviewData) {
        try {
            Map<String, String> result = interviewService.resendInterviewLink(interviewId,coding,interviewData);
            return ResponseEntity.status(200).body(Map.of(
                    "message", "Interview link resent successfully",
                    "statusCode", 200,
                    "link", result.get("link"),
                    "token", result.get("token"),
                    "status", true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "statusCode", 400,
                    "status", false
            ));
        }
    }





}

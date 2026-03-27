package resume.miles.interview.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import resume.miles.interview.dto.InterviewDto;
import resume.miles.interview.dto.InterviewLinkDto;
import resume.miles.interview.dto.InterviewScheduleResponseDto;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.service.InterviewService;

@RestController
@RequestMapping("/api/goodmood/interview")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping(value = "/schedule", consumes = "multipart/form-data")
    public ResponseEntity<?> scheduleInterview(

            @RequestParam String jobId,
            @RequestParam String candidateName,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam MultipartFile resumeFile,
            @RequestParam Boolean isCoding,
            @RequestParam String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String interviewDate

    ) {

        try {

            // Basic validation
            if (resumeFile.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Resume file is required");
            }

            Map<String, String> result = interviewService.scheduleInterview(
                    jobId,
                    candidateName,
                    email,
                    phoneNumber,
                    resumeFile,
                    isCoding,
                    startTime,
                    endTime,
                    interviewDate
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
@PostMapping("/resend-link/{id}")
    public ResponseEntity<?> resendLink(@PathVariable("id") Long interviewId) {
        try {
            String newLink = interviewService.resendInterviewLink(interviewId);
            // return ResponseEntity.ok("Interview link resent successfully: " + newLink);
            return ResponseEntity.status(200).body(Map.of(
                "message","Interview link resent successfully",
                "statusCode",200,
                "link",newLink,
                "status",true
            ));
        } catch (Exception e) {
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            //                      .body("Error resending link: " + e.getMessage());
            return ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "statusCode",400,
                "status",false
            ));
        }
    }





}

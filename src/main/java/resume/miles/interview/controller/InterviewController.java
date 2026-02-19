package resume.miles.interview.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import resume.miles.interview.dto.InterviewScheduleResponseDto;
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

            String link = interviewService.scheduleInterview(
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
                "link",link,
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
                        "message","Something went wrong while schedule interview",
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
                        "statusCode",400
                    ));
        }
    }






}

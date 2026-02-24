package resume.miles.interview.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import resume.miles.interview.dto.InterviewDto;
import resume.miles.interview.dto.InterviewScheduleResponseDto;
import resume.miles.interview.entity.InterviewEntity;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.mapper.InterviewMapper;
import resume.miles.interview.repository.InterviewLinkRepository;
import resume.miles.interview.repository.InterviewRepository;
import resume.miles.jobs.entity.JobEntity;
import resume.miles.jobs.repository.JobRepository;
import resume.miles.question.entity.QuestionEntity;
import resume.miles.question.respository.QuestionRepository;
import resume.miles.recording.entity.VideoRecordingEntity;
import resume.miles.recording.repository.VideoRecodingRepository;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final InterviewLinkRepository interviewLinkRepository;
    private final JobRepository jobRepository;
    private final RestTemplate restTemplate;
    private final QuestionRepository questionRepository;
    private final VideoRecodingRepository videoRecodingRepository;


    private static final String AI_API_URL =
            "https://interviewaiapi.bestworks.cloud/api/v1/process-pdf";

    @Transactional

    public String scheduleInterview(
            String jobId,
            String candidateName,
            String email,
            String phoneNumber,
            MultipartFile resumeFile,
            Boolean isCoding,
            String startTime,
            String endTime,
            String interviewDate
    ) throws IOException {

        // =========================
        // 1️⃣ VALIDATION
        // =========================
        if (resumeFile == null || resumeFile.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }

        if (!resumeFile.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        // =========================
        // 2️⃣ SAVE FILE LOCALLY
        // =========================
        String projectDir = System.getProperty("user.dir");
        String uploadDir = projectDir + File.separator + "uploads";

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFileName = resumeFile.getOriginalFilename()
                .replaceAll("\\s+", "_");

        String fileName = UUID.randomUUID() + "_" + originalFileName;
        File destinationFile = new File(directory, fileName);

        resumeFile.transferTo(destinationFile);

        String filePath = "uploads/" + fileName;

        // =========================
        // 3️⃣ FETCH JOB + JD
        // =========================
        JobEntity job = jobRepository.findById(Long.parseLong(jobId))
                .orElseThrow(() -> new RuntimeException("Job not found"));

        String jd = job.getJd();

        // =========================
        // 4️⃣ CALL AI PROCESS PDF API
        // =========================
       // callAiPdfProcessor(destinationFile, jd);

        // =========================
        // 5️⃣ SAVE INTERVIEW
        // =========================
        InterviewEntity interview = InterviewEntity.builder()
                .jobId(jobId)
                .candidateName(candidateName)
                .email(email)
                .phoneNumber(phoneNumber)
                .resumeLink(filePath)
                .isCoding(isCoding)
                .startTime(LocalTime.parse(startTime))
                .endTime(endTime != null ? LocalTime.parse(endTime) : null)
                .interviewDate(interviewDate != null ? LocalDate.parse(interviewDate) : null)
                .status(1)
                .build();

        InterviewEntity savedInterview = interviewRepository.save(interview);

        List<String> aiQuestions = callAiPdfProcessor(destinationFile, jd);

        for (String question : aiQuestions) {

    QuestionEntity questionEntity = QuestionEntity.builder()
            .candidateJobScheduleId(savedInterview.getId())
            .questions(question)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    questionRepository.save(questionEntity);
}

        // =========================
        // 6️⃣ GENERATE TOKEN + LINK
        // =========================
        String token = UUID.randomUUID().toString();
        String link = "http://localhost:5173/interview/" + token;

        LocalDate parsedInterviewDate = LocalDate.parse(interviewDate);

            LocalTime parsedEndTime;

            // If endTime is provided → use it
            if (endTime != null && !endTime.isEmpty()) {
                parsedEndTime = LocalTime.parse(endTime);
            } else {
                // fallback → 1 hour after startTime
                parsedEndTime = LocalTime.parse(startTime).plusHours(1);
            }

            // Combine to LocalDateTime
            LocalDateTime expiryDateTime =
                    LocalDateTime.of(parsedInterviewDate, parsedEndTime);

        InterviewLinkEntity linkEntity = InterviewLinkEntity.builder()
                .interview(savedInterview)
                .token(token)
                .interviewLink(link)
                .expiryTime(expiryDateTime)
                .isActive(true)
                .build();

        interviewLinkRepository.save(linkEntity);

        return link;
    }


    private List<String> callAiPdfProcessor(File file, String jd) {

    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("pdf_file", new FileSystemResource(file));
        body.add("jd_text", jd);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        AI_API_URL,
                        requestEntity,
                        Map.class
                );

        Map<String, Object> responseBody = response.getBody();

        return (List<String>) responseBody.get("response");

    } catch (Exception e) {
        throw new RuntimeException("AI Processing Failed", e);
    }
}


    @Transactional(readOnly = true)
  public List<InterviewScheduleResponseDto> getAllInterviewSchedules() {

    List<InterviewLinkEntity> interviewLinks =
            interviewLinkRepository.findAll();
            

    return interviewLinks.stream()
            .map(link -> {

                InterviewEntity interview = link.getInterview();
                

                    Optional<VideoRecordingEntity> video =
                        videoRecodingRepository.findByInterviewLinkId(link.getId());

                String videoLink = video.map(VideoRecordingEntity::getVideoLink)
                        .orElse(null);

                JobEntity job = null;

                
                try {
                    Long jobId = Long.parseLong(interview.getJobId());
                    job = jobRepository.findById(jobId).orElse(null);
                } catch (Exception e) {
                    job = null;
                }
                String clientName = null;
                String jobDescription = null;

                if (job != null) {
                    jobDescription = job.getJd();

                    if (job.getClient() != null) {
                        clientName = job.getClient().getClientName();
                    }
                }

                return InterviewScheduleResponseDto.builder()
                        .candidateName(interview.getCandidateName())
                        .candidateEmail(interview.getEmail())
                        .candidatePhone(interview.getPhoneNumber())
                        .resumeLink(interview.getResumeLink())
                        // .jobName(job != null ? job.getRole() : null)
                        .jobDescription(jobDescription)
                        .jobName(clientName)
                        .interviewDate(interview.getInterviewDate())
                        .startTime(interview.getStartTime())
                        .endTime(interview.getEndTime())
                        .interviewLink(link.getInterviewLink())
                        .videoLink(videoLink)
                        .build();
            })
            .toList();
}


// @Transactional(readOnly = true)
// public List<InterviewDto> getCandidatesByJobPrimaryId(Long jobPrimaryId) {

//     // 1️⃣ Check job exists
//     JobEntity job = jobRepository.findById(jobPrimaryId)
//             .orElseThrow(() -> new RuntimeException("Job not found"));

//     // 2️⃣ Fetch candidates using jobId stored in InterviewEntity
//     List<InterviewEntity> interviews =
//     interviewRepository.findByJob_Id(jobPrimaryId);
//             // interviewRepository.findByJob_Id(String.valueOf(jobPrimaryId));

//     if (interviews.isEmpty()) {
//         throw new RuntimeException("No candidates found for this job");
//     }

//     return interviews.stream()
//             .map(InterviewMapper::toDTO)
//             .toList();
// }



}



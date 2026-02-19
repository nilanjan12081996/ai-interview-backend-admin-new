package resume.miles.interview.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import resume.miles.interview.dto.InterviewScheduleResponseDto;
import resume.miles.interview.entity.InterviewEntity;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.repository.InterviewLinkRepository;
import resume.miles.interview.repository.InterviewRepository;
import resume.miles.jobs.entity.JobEntity;
import resume.miles.jobs.repository.JobRepository;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final InterviewLinkRepository interviewLinkRepository;
    private final JobRepository jobRepository;

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
        // ✅ 1. VALIDATION
        // =========================
        if (resumeFile == null || resumeFile.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }

        // Allow only PDF files
        if (!resumeFile.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        // =========================
        // ✅ 2. CREATE UPLOAD DIRECTORY (ABSOLUTE PATH)
        // =========================
        String projectDir = System.getProperty("user.dir");
        String uploadDir = projectDir + File.separator + "uploads";

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // =========================
        // ✅ 3. SAFE FILE NAME
        // =========================
        String originalFileName = resumeFile.getOriginalFilename()
                .replaceAll("\\s+", "_");

        String fileName = UUID.randomUUID() + "_" + originalFileName;

        File destinationFile = new File(directory, fileName);

        // Save file
        resumeFile.transferTo(destinationFile);

        // Save relative path in DB
        String filePath = "uploads/" + fileName;

        // =========================
        // ✅ 4. SAVE candidate_job_schedule
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

        // =========================
        // ✅ 5. GENERATE TOKEN + LINK
        // =========================
        String token = UUID.randomUUID().toString();
        String link = "http://localhost:5173/interview/" + token;

        // =========================
        // ✅ 6. SAVE interview_link
        // =========================
        InterviewLinkEntity linkEntity = InterviewLinkEntity.builder()
                .interview(savedInterview)
                .token(token)
                .interviewLink(link)
                .expiryTime(LocalDateTime.now().plusHours(2)) // 2 hours validity
                .isActive(true)
                .build();

        interviewLinkRepository.save(linkEntity);

        // =========================
        // ✅ 7. RETURN LINK
        // =========================
        return link;
    }
    @Transactional(readOnly = true)
  public List<InterviewScheduleResponseDto> getAllInterviewSchedules() {

    List<InterviewLinkEntity> interviewLinks =
            interviewLinkRepository.findAll();

    return interviewLinks.stream()
            .map(link -> {

                InterviewEntity interview = link.getInterview();
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
                        .jobName(job != null ? job.getRole() : null)
                        .jobDescription(jobDescription)
                        .jobName(clientName)
                        .interviewDate(interview.getInterviewDate())
                        .startTime(interview.getStartTime())
                        .endTime(interview.getEndTime())
                        .interviewLink(link.getInterviewLink())
                        .build();
            })
            .toList();
}






}



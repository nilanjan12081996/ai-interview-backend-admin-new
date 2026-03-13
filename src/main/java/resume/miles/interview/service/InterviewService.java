package resume.miles.interview.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.mail.internet.MimeMessage;
import org.springframework.http.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import resume.miles.analysis.entity.AnalysisEntity;
import resume.miles.analysis.repository.AnalysisRepository;
import resume.miles.interview.dto.InterviewDto;
import resume.miles.interview.dto.InterviewLinkDto;
import resume.miles.interview.dto.InterviewScheduleResponseDto;
import resume.miles.interview.entity.InterviewEntity;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.mapper.InterviewMapper;
import resume.miles.interview.repository.InterviewLinkRepository;
import resume.miles.interview.repository.InterviewRepository;
import resume.miles.jobs.entity.JobEntity;
import resume.miles.jobs.repository.JobRepository;
import resume.miles.mandatoryskill.entity.MandatorySkillEntity;
import resume.miles.mandatoryskill.repository.MandatorySkillRepository;
import resume.miles.musthaveskill.entity.MustHaveSkillEntity;
import resume.miles.musthaveskill.repository.MustHaveSkillRepository;
import resume.miles.question.entity.QuestionEntity;
import resume.miles.question.respository.QuestionRepository;
import resume.miles.recording.entity.VideoRecordingEntity;
import resume.miles.recording.repository.VideoRecodingRepository;
import resume.miles.transcription.entity.TranscriptionEntity;
import resume.miles.transcription.repository.TransciptionRepository;
import resume.miles.transcription.service.TranscriptionService;
import resume.miles.analysis.service.AnalysisService;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final InterviewLinkRepository interviewLinkRepository;
    private final JobRepository jobRepository;
    private final MustHaveSkillRepository mustHaveSkillRepository;
    private final MandatorySkillRepository mandatorySkillRepository;
    private final RestTemplate restTemplate;
    private final QuestionRepository questionRepository;
    private final VideoRecodingRepository videoRecodingRepository;
    private final TransciptionRepository transciptionRepository;
    private final TranscriptionService transcriptFileService;
    private final AnalysisRepository analysisRepository;
    private final AnalysisService analysisService;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    private static final String AI_API_URL =
            "https://aiinterviewpythonmain.bestworks.cloud/api/v1/process-pdf";

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
        List<MustHaveSkillEntity> musthaveSkill= mustHaveSkillRepository.findByJob(Long.parseLong(jobId));
        List<MandatorySkillEntity> mandatorySkill = mandatorySkillRepository.findByJob(Long.parseLong(jobId));
        List<String> musthaveSkillData = new ArrayList<>();
        List<String> mandatorySkillData = new ArrayList<>();
        
        for(MustHaveSkillEntity skill : musthaveSkill){
            musthaveSkillData.add(skill.getSkillName());
        }

        for(MandatorySkillEntity skill : mandatorySkill){
            mandatorySkillData.add(skill.getSkillName());
        }

        String finalSkillsMusthaveSkill = String.join(", ", musthaveSkillData);
        String finalSkillsMandatorySkill = String.join(", ", mandatorySkillData);

        System.out.println("musthaveSkill"+finalSkillsMusthaveSkill);
         System.out.println("mandatorySkill"+finalSkillsMandatorySkill);


// Output: "Java, Spring Boot, SQL"

        String jd = job.getJd();
        String experience=job.getExperience();
         String musthaveSkillfinal=finalSkillsMusthaveSkill;
         String mandatorySkillfinal=finalSkillsMandatorySkill;

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

        List<String> aiQuestions = callAiPdfProcessor(destinationFile, jd,experience,musthaveSkillfinal,mandatorySkillfinal);

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
        // String link = "http://localhost:5173/interview/" + token;
        String link = "https://aiinterviewpythonfront.bestworks.cloud/" + token;

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
                .is_complete(0)
                .isActive(true)
                .build();

        interviewLinkRepository.save(linkEntity);
        try {
            Context context = new Context();
            context.setVariable("candidateName", candidateName);
            context.setVariable("interviewLink", link);
            System.out.println("interviewLink"+link);
            context.setVariable("jobTitle", job.getRole()); // Ensure job title is passed to the template

            String process = templateEngine.process("interviewLinkSend", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("iksen.testmail@gmail.com");
            // Ensure you use the 'email' variable passed to the method, not 'toEmail'
            helper.setTo(email);
            helper.setSubject("Invitation: AI Interview");
            helper.setText(process, true); // Set true for HTML

            mailSender.send(message);
            System.out.println("Email successfully sent to: " + email);

        } catch (jakarta.mail.MessagingException e) {
            // You can choose to throw a RuntimeException or log the error.
            // Throwing a RuntimeException will rollback the transaction if the email fails.
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Failed to send interview invitation email", e);
        }
        return link;
    }


    private List<String> callAiPdfProcessor(File file, String jd,String experience,String musthaveSkillfinal,String mandatorySkillfinal) {
        
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("pdf_file", new FileSystemResource(file));
        body.add("jd_text", jd);
        body.add("Experience", experience);
        body.add("Nice_to_have_skills", musthaveSkillfinal);
        body.add("Mandatory_skills", mandatorySkillfinal);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        AI_API_URL,
                        requestEntity,
                        Map.class
                );

        Map<String, Object> responseBody = response.getBody();
        System.out.println("response.getBody()"+response.getBody());

        return (List<String>) responseBody.get("response");

    } catch (Exception e) {
        throw new RuntimeException("AI Processing Failed", e);
    }
}


//     @Transactional(readOnly = true)
//   public List<InterviewScheduleResponseDto> getAllInterviewSchedules() {

//     List<InterviewLinkEntity> interviewLinks =
//             interviewLinkRepository.findAll();
            

//     return interviewLinks.stream()
//             .map(link -> {

//                 InterviewEntity interview = link.getInterview();
                

//                     Optional<VideoRecordingEntity> video =
//                         videoRecodingRepository.findByInterviewLinkId(link.getId());

//                 String videoLink = video.map(VideoRecordingEntity::getVideoLink)
//                         .orElse(null);
                
                

//                         Optional<TranscriptionEntity> transcription =
//                         transciptionRepository
//                                 .findTopByInterviewLinkIdOrderByCreatedAtDesc(link.getId());

//                         String transcriptFileLink = null;

//                     if (transcription.isPresent()) {

//                     transcriptFileLink = transcriptFileService
//                             .generateTranscriptFile(
//                                     link.getId(),
//                                     transcription.get().getTranscript()
//                             );
//                 }

//                 Optional<AnalysisEntity> analysis =
//                             analysisRepository
//                                     .findTopByInterviewLinkIdOrderByCreatedAtDesc(link.getId());

//                     String analysisFileLink = null;

//                     if (analysis.isPresent()) {

//                        analysisFileLink = analysisService
//             .generateAnalysisPdf(
//                     link.getId(),
//                     analysis.get().getAnalysis(),
//                     interview.getCandidateName(),   // name
//                     interview.getEmail(),           // email
//                     interview.getPhoneNumber(),     // phone
//                     link.getInterviewLink(),        // interview URL
//                     interview.getInterviewDate() != null ? interview.getInterviewDate().toString() : null,
//                     "30min",
//                     videoLink,
//                     transcriptFileLink
                    

                   
//             );
//                     }
//                 JobEntity job = null;

                
//                 try {
//                     Long jobId = Long.parseLong(interview.getJobId());
//                     job = jobRepository.findById(jobId).orElse(null);
//                 } catch (Exception e) {
//                     job = null;
//                 }
//                 String clientName = null;
//                 String jobDescription = null;

//                 if (job != null) {
//                     jobDescription = job.getJd();

//                     if (job.getClient() != null) {
//                         clientName = job.getClient().getClientName();
//                     }
//                 }

//                 return InterviewScheduleResponseDto.builder()
//                         .id(interview.getId())
//                         .candidateName(interview.getCandidateName())
//                         .candidateEmail(interview.getEmail())
//                         .candidatePhone(interview.getPhoneNumber())
//                         .resumeLink(interview.getResumeLink())
//                         // .jobName(job != null ? job.getRole() : null)
//                         .jobDescription(jobDescription)
//                         .jobName(clientName)
//                         .interviewDate(interview.getInterviewDate())
//                         .startTime(interview.getStartTime())
//                         .endTime(interview.getEndTime())
//                         .interviewLink(link.getInterviewLink())
//                         .transcription(transcriptFileLink)
//                          .analysis(analysisFileLink)
//                         .videoLink(videoLink)
//                         .is_complete(link.getIs_complete())
                        
//                         .build();
//             })
//             .toList();
// }



@Transactional(readOnly = true)
public List<InterviewScheduleResponseDto> getAllInterviewSchedules() {

    // 1️⃣ Fetch all distinct interviews (candidates) instead of all links
    // This prevents duplicate rows in the UI if a candidate has multiple links
    List<InterviewEntity> allInterviews = interviewRepository.findAll();

    return allInterviews.stream().map(interview -> {

        // 2️⃣ Fetch ALL links for this specific interview
        List<InterviewLinkEntity> allLinks = interviewLinkRepository.findAllByInterview(interview);
        
        // Sort them so newest links (highest ID) are checked first
        allLinks.sort((l1, l2) -> l2.getId().compareTo(l1.getId()));

        // 3️⃣ Find the currently ACTIVE link for the URL and Status
        InterviewLinkEntity activeLink = allLinks.stream()
                .filter(InterviewLinkEntity::getIsActive)
                .findFirst()
                .orElse(null);

        String interviewUrl = activeLink != null ? activeLink.getInterviewLink() : null;
        // Integer isComplete = activeLink != null ? activeLink.getIs_complete() : 0;
        Integer isComplete = allLinks.stream()
        .anyMatch(l -> l.getIs_complete() != null && l.getIs_complete() == 1) ? 1 : 0;

        // Variables to hold our historical data
        String videoLink = null;
        String transcriptFileLink = null;
        String analysisFileLink = null;
        String terminationCause = null;
        String userJustification = null;
        String duration = null;

        // 4️⃣ Loop through past links to find the most recent resources
        for (InterviewLinkEntity pastLink : allLinks) {
            Long linkId = pastLink.getId();

            if (terminationCause == null && pastLink.getTerminationCause() != null) {
                terminationCause = pastLink.getTerminationCause();
            }

            // 🔹 Find User Justification (if we haven't found one yet)
            if (userJustification == null && pastLink.getUserJustification() != null) {
                userJustification = pastLink.getUserJustification();
            }

            // 🔹 Find Video
            if (videoLink == null) {
                videoLink = videoRecodingRepository.findByInterviewLinkId(linkId)
                        .map(VideoRecordingEntity::getVideoLink).orElse(null);
            }

            // 🔹 Find Transcription
            if (transcriptFileLink == null) {
                Optional<TranscriptionEntity> transcription = transciptionRepository
                        .findTopByInterviewLinkIdOrderByCreatedAtDesc(linkId);
                if (transcription.isPresent()) {
                    transcriptFileLink = transcriptFileService.generateTranscriptFile(
                            linkId, transcription.get().getTranscript()
                    );
                }
            }

            // 🔹 Find Analysis
            if (analysisFileLink == null) {
                Optional<AnalysisEntity> analysis = analysisRepository
                        .findTopByInterviewLinkIdOrderByCreatedAtDesc(linkId);
                if (analysis.isPresent()) {
                    AnalysisEntity analysisDuration = analysis.get();
                    duration = analysisDuration.getDuration();
                    analysisFileLink = analysisService.generateAnalysisPdf(
                            linkId,
                            analysis.get().getAnalysis(),
                            interview.getCandidateName(),
                            interview.getEmail(),
                            interview.getPhoneNumber(),
                            pastLink.getInterviewLink(), 
                            interview.getInterviewDate() != null ? interview.getInterviewDate().toString() : null,
                            duration,
                            videoLink,
                            transcriptFileLink
                    );
                }
            }

            // Stop searching if we've found all three files
            if (videoLink != null && transcriptFileLink != null && analysisFileLink != null && 
                terminationCause != null && userJustification != null) {
                break;
            }
        }

        // 5️⃣ Fetch Job Info safely
        JobEntity job = null;
        try {
            if (interview.getJobId() != null) {
                Long jobId = Long.parseLong(interview.getJobId());
                job = jobRepository.findById(jobId).orElse(null);
            }
        } catch (NumberFormatException e) {
            // Fails gracefully if jobId is not a valid number
            job = null; 
        }

        String clientName = (job != null && job.getClient() != null) ? job.getClient().getClientName() : null;
        String jobDescription = job != null ? job.getJd() : null;

        // 6️⃣ Return the DTO
        return InterviewScheduleResponseDto.builder()
                .id(interview.getId()) // Ensure the ID is mapped for the "Resend" button
                .candidateName(interview.getCandidateName())
                .candidateEmail(interview.getEmail())
                .candidatePhone(interview.getPhoneNumber())
                .resumeLink(interview.getResumeLink())
                .jobDescription(jobDescription)
                .jobName(clientName)
                .interviewDate(interview.getInterviewDate())
                .startTime(interview.getStartTime())
                .endTime(interview.getEndTime())
                .interviewLink(interviewUrl)         // The fresh, new active link
                .transcription(transcriptFileLink)   // The historical transcription
                .analysis(analysisFileLink)          // The historical analysis
                .videoLink(videoLink)                // The historical video
                .terminationCause(terminationCause)     // Added
                .userJustification(userJustification)   // Added
                .is_complete(isComplete)
                .build();

    }).toList();
}



@Transactional(readOnly = true)
public InterviewLinkDto getAllList(String token){

    Optional<InterviewLinkEntity> entity = interviewLinkRepository.findByTokenAndIsActiveTrue(token);
    if(entity.isEmpty()){
        throw new RuntimeException("invalid token" + token);
    }
     InterviewLinkDto interviewLinkDto = InterviewLinkDto.builder()
                                        .id(entity.get().getId())
                                        .interviewDto(InterviewMapper.toDTONew(entity.get().getInterview()))
                                        .build();
    return interviewLinkDto;

}


@Transactional(readOnly = true)
public List<InterviewScheduleResponseDto> getCandidatesByJobPrimaryId(Long jobPrimaryId) {

    // 1️⃣ Check job exists
    JobEntity job = jobRepository.findById(jobPrimaryId)
            .orElseThrow(() -> new RuntimeException("Job not found"));

    String jobIdString = String.valueOf(jobPrimaryId);

    // 2️⃣ Fetch interviews
    List<InterviewEntity> interviews =
            interviewRepository.findByJobId(jobIdString);

  



return interviews.stream().map(interview -> {

    // 1️⃣ Fetch ALL links for this interview
    List<InterviewLinkEntity> allLinks = interviewLinkRepository.findAllByInterview(interview);
    
    // Sort them so newest links (highest ID) are checked first
    allLinks.sort((l1, l2) -> l2.getId().compareTo(l1.getId()));

    // 2️⃣ Find the currently ACTIVE link for the Resend/Interview URL
    InterviewLinkEntity activeLink = allLinks.stream()
            .filter(InterviewLinkEntity::getIsActive)
            .findFirst()
            .orElse(null);

    String interviewUrl = activeLink != null ? activeLink.getInterviewLink() : null;
//     Integer isComplete = activeLink != null ? activeLink.getIs_complete() : 0;
// Check if ANY of the candidate's links (past or present) were completed
Integer isComplete = allLinks.stream()
        .anyMatch(l -> l.getIs_complete() != null && l.getIs_complete() == 1) ? 1 : 0;

    // Variables to hold our historical data
    String videoLink = null;
    String transcriptFileLink = null;
    String analysisFileLink = null;
    String terminationCause = null;
    String userJustification = null;
    String duration = null;

    // 3️⃣ Loop through all past and present links to find the resources
    for (InterviewLinkEntity pastLink : allLinks) {
        Long linkId = pastLink.getId();

        // 🔹 Find Termination Cause (if we haven't found one yet)
            if (terminationCause == null && pastLink.getTerminationCause() != null) {
                terminationCause = pastLink.getTerminationCause();
            }
            if (userJustification == null && pastLink.getUserJustification() != null) {
                userJustification = pastLink.getUserJustification();
            }

        // 🔹 Find Video (if we haven't found one yet)
        if (videoLink == null) {
            videoLink = videoRecodingRepository.findByInterviewLinkId(linkId)
                    .map(VideoRecordingEntity::getVideoLink).orElse(null);
        }

        // 🔹 Find Transcription (if we haven't found one yet)
        if (transcriptFileLink == null) {
            Optional<TranscriptionEntity> transcription = transciptionRepository
                    .findTopByInterviewLinkIdOrderByCreatedAtDesc(linkId);
            if (transcription.isPresent()) {
                transcriptFileLink = transcriptFileService.generateTranscriptFile(
                        linkId, transcription.get().getTranscript()
                );
            }
        }

        // 🔹 Find Analysis (if we haven't found one yet)
        if (analysisFileLink == null) {
            Optional<AnalysisEntity> analysis = analysisRepository
                    .findTopByInterviewLinkIdOrderByCreatedAtDesc(linkId);
                  
            if (analysis.isPresent()) {
                  AnalysisEntity analysisDuration = analysis.get();
                  duration = analysisDuration.getDuration();
                analysisFileLink = analysisService.generateAnalysisPdf(
                        linkId,
                        analysis.get().getAnalysis(),
                        interview.getCandidateName(),
                        interview.getEmail(),
                        interview.getPhoneNumber(),
                        pastLink.getInterviewLink(), // Pass the link where the interview actually happened
                        interview.getInterviewDate() != null ? interview.getInterviewDate().toString() : null,
                        duration,
                        videoLink,
                        transcriptFileLink
                );
            }
        }

        // Stop searching if we've found all three files
        if (videoLink != null && transcriptFileLink != null && analysisFileLink != null && 
                terminationCause != null && userJustification != null) {
                break;
            }
    }

    // 4️⃣ Return the combined Data Transfer Object
    return InterviewScheduleResponseDto.builder()
            .id(interview.getId())
            .candidateName(interview.getCandidateName())
            .candidateEmail(interview.getEmail())
            .candidatePhone(interview.getPhoneNumber())
            .resumeLink(interview.getResumeLink())
            .jobDescription(job.getJd())
            .jobName(job.getClient() != null ? job.getClient().getClientName() : null)
            .interviewDate(interview.getInterviewDate())
            .startTime(interview.getStartTime())
            .endTime(interview.getEndTime())
            .interviewLink(interviewUrl)         // The fresh, new active link
            .transcription(transcriptFileLink)   // The historical transcription
            .analysis(analysisFileLink)          // The historical analysis
            .videoLink(videoLink)
            .terminationCause(terminationCause)     // Added
            .userJustification(userJustification)                // The historical video
            .is_complete(isComplete)
            .build();

}).toList();








}





@Transactional
public String resendInterviewLink(Long interviewId) {
    // 1. Fetch the existing interview
    InterviewEntity interview = interviewRepository.findById(interviewId)
            .orElseThrow(() -> new RuntimeException("Interview not found with ID: " + interviewId));

    // 2. Optional: Deactivate old links if any
    List<InterviewLinkEntity> oldLinks = interviewLinkRepository.findAllByInterview(interview);
    for (InterviewLinkEntity oldLink : oldLinks) {
        oldLink.setIsActive(false);
    }
    interviewLinkRepository.saveAll(oldLinks);

    // 3. Generate New Token and Link
    String newToken = UUID.randomUUID().toString();
    String newLink = "https://aiinterviewpython.bestworks.cloud/" + newToken;

    // 4. Calculate Expiry (using your existing logic: 1 hour after start if end not present)
    LocalDateTime expiryDateTime;
    if (interview.getEndTime() != null) {
        expiryDateTime = LocalDateTime.of(interview.getInterviewDate(), interview.getEndTime());
    } else {
        expiryDateTime = LocalDateTime.of(interview.getInterviewDate(), interview.getStartTime().plusHours(1));
    }

    // 5. Save New Link Entity
    InterviewLinkEntity linkEntity = InterviewLinkEntity.builder()
            .interview(interview)
            .token(newToken)
            .interviewLink(newLink)
            .expiryTime(expiryDateTime)
            .is_complete(0)
            .isActive(true)
            .build();

    interviewLinkRepository.save(linkEntity);

    // 6. Send Email (Reusing your logic)
    try {
        JobEntity job = jobRepository.findById(Long.parseLong(interview.getJobId()))
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Context context = new Context();
        context.setVariable("candidateName", interview.getCandidateName());
        context.setVariable("interviewLink", newLink);
        context.setVariable("jobTitle", job.getRole());

        String emailContent = templateEngine.process("interviewLinkSend", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("iksen.testmail@gmail.com");
        helper.setTo(interview.getEmail());
        helper.setSubject("Resent Invitation: AI Interview");
        helper.setText(emailContent, true);

        mailSender.send(message);
        
        return newLink;
    } catch (Exception e) {
        throw new RuntimeException("Failed to resend interview invitation email", e);
    }
}
}




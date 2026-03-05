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
import org.springframework.http.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
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
        String link = "https://aiinterviewpython.bestworks.cloud/" + token;

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
                
                

                        Optional<TranscriptionEntity> transcription =
                        transciptionRepository
                                .findTopByInterviewLinkIdOrderByCreatedAtDesc(link.getId());

                        String transcriptFileLink = null;

                    if (transcription.isPresent()) {

                    transcriptFileLink = transcriptFileService
                            .generateTranscriptFile(
                                    link.getId(),
                                    transcription.get().getTranscript()
                            );
                }

                Optional<AnalysisEntity> analysis =
                            analysisRepository
                                    .findTopByInterviewLinkIdOrderByCreatedAtDesc(link.getId());

                    String analysisFileLink = null;

                    if (analysis.isPresent()) {

                       analysisFileLink = analysisService
            .generateAnalysisPdf(
                    link.getId(),
                    analysis.get().getAnalysis(),
                    interview.getCandidateName(),   // name
                    interview.getEmail(),           // email
                    interview.getPhoneNumber(),     // phone
                    link.getInterviewLink(),        // interview URL
                    interview.getInterviewDate() != null ? interview.getInterviewDate().toString() : null
            );
                    }
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
                        .transcription(transcriptFileLink)
                         .analysis(analysisFileLink)
                        .videoLink(videoLink)
                       
                        .build();
            })
            .toList();
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

// @Transactional(readOnly = true)
// public List<InterviewDto> getCandidatesByJobPrimaryId(Long jobPrimaryId) {

//     // 1️⃣ Check job exists
//     jobRepository.findById(jobPrimaryId)
//             .orElseThrow(() -> new RuntimeException("Job not found"));

//     // 2️⃣ Convert Long to String (VERY IMPORTANT)
//     String jobIdString = String.valueOf(jobPrimaryId);

//     // 3️⃣ Fetch candidates
//     List<InterviewEntity> interviews =
//             interviewRepository.findByJobId(jobIdString);

//     if (interviews.isEmpty()) {
//         throw new RuntimeException("No candidates assigned to this job");
//     }

//     return interviews.stream()
//             .map(InterviewMapper::toDTO)
//             .toList();
// }










@Transactional(readOnly = true)
public List<InterviewScheduleResponseDto> getCandidatesByJobPrimaryId(Long jobPrimaryId) {

    // 1️⃣ Check job exists
    JobEntity job = jobRepository.findById(jobPrimaryId)
            .orElseThrow(() -> new RuntimeException("Job not found"));

    String jobIdString = String.valueOf(jobPrimaryId);

    // 2️⃣ Fetch interviews
    List<InterviewEntity> interviews =
            interviewRepository.findByJobId(jobIdString);

    // if (interviews.isEmpty()) {
    //     throw new RuntimeException("No candidates assigned to this job");
    // }

    return interviews.stream().map(interview -> {

        // 🔹 Fetch Interview Link
        InterviewLinkEntity link =
                interviewLinkRepository.findByInterview(interview)
                        .orElse(null);

        String videoLink = null;
        String transcriptFileLink = null;
        String analysisFileLink = null;
        String interviewUrl = null;

        if (link != null) {

            interviewUrl = link.getInterviewLink();

            // 🔹 Video
            videoLink = videoRecodingRepository
                    .findByInterviewLinkId(link.getId())
                    .map(VideoRecordingEntity::getVideoLink)
                    .orElse(null);

            // 🔹 Transcription
            Optional<TranscriptionEntity> transcription =
                    transciptionRepository
                            .findTopByInterviewLinkIdOrderByCreatedAtDesc(link.getId());

            if (transcription.isPresent()) {
                transcriptFileLink = transcriptFileService
                        .generateTranscriptFile(
                                link.getId(),
                                transcription.get().getTranscript()
                        );
            }

            // 🔹 Analysis
            Optional<AnalysisEntity> analysis =
                    analysisRepository
                            .findTopByInterviewLinkIdOrderByCreatedAtDesc(link.getId());

            if (analysis.isPresent()) {
                analysisFileLink = analysisService
                        .generateAnalysisPdf(
                                link.getId(),
                                analysis.get().getAnalysis(),
                                interview.getCandidateName(),
                                interview.getEmail(),
                                interview.getPhoneNumber(),
                                interviewUrl,
                                interview.getInterviewDate() != null
                                        ? interview.getInterviewDate().toString()
                                        : null
                        );
            }
        }

        return InterviewScheduleResponseDto.builder()
                .candidateName(interview.getCandidateName())
                .candidateEmail(interview.getEmail())
                .candidatePhone(interview.getPhoneNumber())
                .resumeLink(interview.getResumeLink())
                .jobDescription(job.getJd())
                .jobName(job.getClient() != null
                        ? job.getClient().getClientName()
                        : null)
                .interviewDate(interview.getInterviewDate())
                .startTime(interview.getStartTime())
                .endTime(interview.getEndTime())
                .interviewLink(interviewUrl)
                .transcription(transcriptFileLink)
                .analysis(analysisFileLink)
                .videoLink(videoLink)
                .build();

    }).toList();
}



}



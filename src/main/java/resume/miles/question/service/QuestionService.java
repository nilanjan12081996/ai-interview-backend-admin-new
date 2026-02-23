package resume.miles.question.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.repository.InterviewLinkRepository;
import resume.miles.question.dto.QuestionResponseDTO;
import resume.miles.question.entity.QuestionEntity;
import resume.miles.question.mapper.QuestionMapper;
import resume.miles.question.respository.QuestionRepository;
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final InterviewLinkRepository interviewLinkRepository;
    private final QuestionRepository questionRepository;

    public List<QuestionResponseDTO> getQuestionsByToken(String token) {

    InterviewLinkEntity link = interviewLinkRepository
            .findByTokenAndIsActiveTrue(token)
            .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

    if (link.getExpiryTime().isBefore(LocalDateTime.now())) {
        throw new RuntimeException("Token expired");
    }

    Long interviewId = link.getInterview().getId();

    List<QuestionEntity> questionEntities =
            questionRepository.findByCandidateJobScheduleId(interviewId);

    return questionEntities.stream()
            .map(QuestionMapper::toDTO)
            .toList();
}
    
}

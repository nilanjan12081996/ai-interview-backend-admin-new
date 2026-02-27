package resume.miles.analysis.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import resume.miles.analysis.dto.AnalysisRequestDto;
import resume.miles.analysis.entity.AnalysisEntity;
import resume.miles.analysis.repository.AnalysisRepository;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.repository.InterviewLinkRepository;
import resume.miles.transcription.entity.TranscriptionEntity;

@Service
@RequiredArgsConstructor
public class  AnalysisService{
    private final InterviewLinkRepository interviewLinkRepository;
    private final AnalysisRepository analysisRepository;

    public void saveAnalysis(AnalysisRequestDto request){
        InterviewLinkEntity interviewLink = interviewLinkRepository.findByToken(request.getToken())
            .orElseThrow(() -> new RuntimeException("Invalid token"));

    AnalysisEntity analysis = AnalysisEntity.builder()
            .interviewLinkId(interviewLink.getId())
            .analysis(request.getAnalysis())
            .status(1)
            .build();

    analysisRepository.save(analysis);
    }
}



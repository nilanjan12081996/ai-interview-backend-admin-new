package resume.miles.transcription.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.repository.InterviewLinkRepository;
import resume.miles.transcription.dto.TranscriptionRequestDto;
import resume.miles.transcription.entity.TranscriptionEntity;
import resume.miles.transcription.repository.TransciptionRepository;

@Service
@RequiredArgsConstructor
public class TranscriptionService {
    private final InterviewLinkRepository interviewLinkRepository;
    private final TransciptionRepository transciptionRepository;

    public void saveTranscription(TranscriptionRequestDto request){
        InterviewLinkEntity interviewLink = interviewLinkRepository.findByToken(request.getToken())
            .orElseThrow(() -> new RuntimeException("Invalid token"));

    TranscriptionEntity transcription = TranscriptionEntity.builder()
            .interviewLinkId(interviewLink.getId())
            .transcript(request.getTranscript())
            .status(1)
            .build();

    transciptionRepository.save(transcription);
    }
}

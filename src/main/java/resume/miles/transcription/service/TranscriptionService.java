package resume.miles.transcription.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import resume.miles.config.TranscriptionFormatter;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.repository.InterviewLinkRepository;
import resume.miles.transcription.dto.TranscriptionRequestDto;
import resume.miles.transcription.entity.TranscriptionEntity;
import resume.miles.transcription.repository.TransciptionRepository;

@Service
@RequiredArgsConstructor
public class TranscriptionService {
     private static final String BASE_DIR = "uploads/transcripts/";
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



    public String generateTranscriptFile(Long interviewLinkId, String transcript) {

        try {
            File directory = new File(BASE_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = "interview_" + interviewLinkId + ".txt";
            String fullPath = BASE_DIR + fileName;

            String formatted = TranscriptionFormatter.formatTranscript(transcript);

            FileWriter writer = new FileWriter(fullPath, false); // overwrite mode
            writer.write(formatted);
            writer.close();

            // 🔥 IMPORTANT → return public URL path (not full system path)
            return "/uploads/transcripts/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to create transcript file", e);
        }
    }

}

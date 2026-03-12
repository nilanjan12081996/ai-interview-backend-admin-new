package resume.miles.transcription.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import resume.miles.transcription.dto.TranscriptionRequestDto;
import resume.miles.transcription.service.TranscriptionService;

@RestController
@RequestMapping("/transcript/ai")
@RequiredArgsConstructor
public class TranscriptionController {
    private final TranscriptionService transcriptionService;

    @PostMapping
    public ResponseEntity<?> saveTranscription(@RequestBody TranscriptionRequestDto request){
        try {
            transcriptionService.saveTranscription(request);
            return ResponseEntity.status(201).body(Map.of(
                "message","Transaction saved successfully",
                "status",true,
                "statusCode",201
            ));
        } catch (Exception e) {
             return ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "status",false,
                "statusCode",400
            ));
        }
    }
}

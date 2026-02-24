package resume.miles.question.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import resume.miles.question.dto.QuestionResponseDTO;
import resume.miles.question.service.QuestionService;

@RestController
@RequestMapping("/api/goodmood/question")
@RequiredArgsConstructor
public class QuestionContoller {
    private final QuestionService questionService;
    private final RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String openApi;
@GetMapping("/get-question/{token}")
public ResponseEntity<?> getQuestion(@PathVariable String token)
{
    try {
       List<QuestionResponseDTO> questions= questionService.getQuestionsByToken(token);
       return ResponseEntity.status(200).body(Map.of(
        "message","Question fetch successfully",
        "status",true,
        "statusCode",200,
        "questions",questions
       ));
    } catch (Exception e) {
      return ResponseEntity.status(400).body(Map.of(
        "message",e.getMessage(),
        "status",false,
        "statusCode",400
      ));
    }

}

@PostMapping("/openai/session")
public ResponseEntity<?> createSession() {

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(openApi);
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, Object> body = new HashMap<>();
    body.put("model", "gpt-4o-realtime-preview-2024-12-17");

    // Text modality only — browser handles all TTS via SpeechSynthesis API.
    // OpenAI Realtime is used ONLY for VAD (detecting when user stops speaking)
    // and Whisper transcription. No AI voice output needed.
    body.put("modalities", new String[]{"text"});

    // Lock the AI: do not generate conversational responses
    body.put("instructions",
        "You are a silent transcription service. " +
        "Do NOT generate any responses. Do NOT speak. Do NOT answer questions. " +
        "Your only job is to transcribe what the user says. Stay completely silent."
    );

    // Enable Whisper transcription so transcription.completed events fire
    Map<String, Object> transcription = new HashMap<>();
    transcription.put("model", "whisper-1");
    body.put("input_audio_transcription", transcription);

    // Server VAD: detects when user stops speaking
    Map<String, Object> turnDetection = new HashMap<>();
    turnDetection.put("type", "server_vad");
    turnDetection.put("threshold", 0.5);
    turnDetection.put("prefix_padding_ms", 300);
    turnDetection.put("silence_duration_ms", 1000);
    body.put("turn_detection", turnDetection);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

    try {
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.openai.com/v1/realtime/sessions",
                HttpMethod.POST,
                entity,
                String.class
        );
        return ResponseEntity.ok(response.getBody());
    } catch (HttpClientErrorException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(Map.of("error", e.getResponseBodyAsString()));
    } catch (Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
    }
}






}

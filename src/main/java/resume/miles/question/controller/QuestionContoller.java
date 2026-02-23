package resume.miles.question.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
// public ResponseEntity<?> createRealtimeSession() {

//     HttpHeaders headers = new HttpHeaders();
//     headers.setContentType(MediaType.APPLICATION_JSON);
//     headers.setBearerAuth(openApi); // sk- key

//     Map<String, Object> body = new HashMap<>();
//     body.put("model", "gpt-4o-realtime-preview");
//     body.put("voice", "sage");

//     // 🔥 Force English only responses
//     body.put("instructions",
//             "You are a professional AI interviewer. " +
//             "You must speak ONLY English at all times. " +
//             "Never switch languages. " +
//             "If the user speaks another language, politely ask them to speak English."
//     );

//     // 🔥 Required: transcription model + language
//     Map<String, Object> audioInput = new HashMap<>();
//     audioInput.put("model", "whisper-1");   // REQUIRED
//     audioInput.put("language", "en");

//     body.put("input_audio_transcription", audioInput);

//     HttpEntity<Map<String, Object>> request =
//             new HttpEntity<>(body, headers);

//     ResponseEntity<Map> response = restTemplate.postForEntity(
//             "https://api.openai.com/v1/realtime/sessions",
//             request,
//             Map.class
//     );

//     Map<String, Object> responseBody = response.getBody();
//     Map<String, Object> clientSecret =
//             (Map<String, Object>) responseBody.get("client_secret");

//     return ResponseEntity.ok(Map.of(
//             "client_secret", clientSecret.get("value"),
//             "statusCode", 200
//     ));
// }


 public ResponseEntity<?> createSession() {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openApi);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-realtime-preview");

        // 🔒 STRICT MODE — NO FREE CONVERSATION
        body.put("instructions",
                "You are a strict AI interviewer. " +
                "You are NOT allowed to create your own questions. " +
                "You must ONLY read the exact question text provided in response.create. " +
                "You must not add anything extra. " +
                "If no question is provided, remain silent."
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.openai.com/v1/realtime/sessions",
                HttpMethod.POST,
                entity,
                String.class
        );

         return ResponseEntity.ok(response.getBody());
    }

}

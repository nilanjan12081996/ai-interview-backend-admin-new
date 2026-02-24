package resume.miles.recording.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import resume.miles.recording.service.VideoRecodingService;

@RestController
@RequestMapping("api/goodmood/recording")
@RequiredArgsConstructor
public class VideoRecordingController {
    private final VideoRecodingService recordingService;

    @PostMapping(value="/upload-recording", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

       public ResponseEntity<?> uploadRecording(
            @RequestParam("recording") MultipartFile recording,
            @RequestParam("token") String token
    ) {
        try {
            String videoUrl = recordingService.saveRecording(token, recording);

            return ResponseEntity.ok(Map.of(
                    "statusCode", 200,
                    "message", "Recording uploaded successfully",
                    "videoLink", videoUrl
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "statusCode", 400,
                    "message", e.getMessage()
            ));
        }
    }
}

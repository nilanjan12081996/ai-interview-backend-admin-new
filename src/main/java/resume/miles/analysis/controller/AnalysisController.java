package resume.miles.analysis.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import resume.miles.analysis.dto.AnalysisRequestDto;
import resume.miles.analysis.service.AnalysisService;


@RestController
@RequestMapping("/analysis/ai")
@RequiredArgsConstructor
public class AnalysisController {
    private final AnalysisService analysisService;
     @PostMapping("/")
    public ResponseEntity<?> saveAnalysis(@RequestBody AnalysisRequestDto request){
        try {
            analysisService.saveAnalysis(request);
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

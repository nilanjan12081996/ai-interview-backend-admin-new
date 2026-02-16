package resume.miles.moodEqualizer.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import resume.miles.moodEqualizer.dto.MoodEqualizerDTO;
import resume.miles.moodEqualizer.service.MoodEqualizerService;

@RestController
@RequestMapping("/api/goodmood/equalizer")
public class MoodEqualizerController {
    @Value("${urls.baseUrl}")
    private String baseUrl;
    @Autowired
    private MoodEqualizerService moodEqualizerService;

   @PostMapping("/upload")
    public ResponseEntity<?> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "chunkIndex", required = false) Integer chunkIndex,
            @RequestParam(value = "totalChunks", required = false) Integer totalChunks,
            @RequestParam(value = "fileKey", required = false) String fileKey,
            @RequestParam(value = "originalName", required = false) String originalName,
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "name", required = false) String name
    ) {
        try {
            if (chunkIndex != null && totalChunks != null) {
       
                Map<String, String> uploadResult = moodEqualizerService.saveChunk(file, chunkIndex, totalChunks, fileKey, originalName);

                if (uploadResult != null) {
                    
                 
                    String videoUrl = uploadResult.get("videoUrl");
                    String thumbUrl = uploadResult.get("thumbnailUrl");
                    
                 
                    moodEqualizerService.saveDatabase(videoUrl, thumbUrl, id,name);

                    return ResponseEntity.ok(Map.of(
                        "status", true, 
                        "message", "Upload complete", 
                        "type", "chunk",
                        "videoUrl", videoUrl,
                        "thumbnailUrl", thumbUrl ,
                        "statusCode",200
                    ));
                } else {
                   
                    return ResponseEntity.ok(Map.of(
                        "status", true, 
                        "message", "Chunk saved", 
                        "chunkIndex", chunkIndex, 
                        "type", "chunk",
                        "statusCode",200
                    ));
                }

            } else {
                String fileUrl = moodEqualizerService.saveStandardFile(file,id,name);
                return ResponseEntity.ok(Map.of(
                    "status", true, 
                    "message", "File uploaded", 
                    "url", fileUrl, 
                    "type", "standard",
                    "statusCode",200
                ));
            }

        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(400).body(Map.of(
                "status", false, 
                "message", e.getMessage(),
                "statusCode",400
            ));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(required = false) Long id,@RequestParam(required = false) Long awid){
        try {

                 List<MoodEqualizerDTO>   data = moodEqualizerService.findAllData(id,awid);
                 return ResponseEntity.ok(Map.of(
                        "status", true, 
                        "message", "data found", 
                        "statusCode",200,
                        "data",data,
                        "baseUrl",baseUrl
                    ));
        }catch(Exception e){
             return ResponseEntity.status(400).body(Map.of(
                "status", false, 
                "message", e.getMessage(),
                "statusCode",400
            ));
        }
    }


     @PatchMapping("/banner/update")
    public ResponseEntity<?> image(@RequestParam(required = true) Long id,@RequestParam("file") MultipartFile file){
        try {

                 moodEqualizerService.updateBanner(file,id);
                 return ResponseEntity.ok(Map.of(
                        "status", true, 
                        "message", "Thumbnail updated", 
                        "statusCode",200
                    ));
        }catch(Exception e){
             return ResponseEntity.status(400).body(Map.of(
                "status", false, 
                "message", e.getMessage(),
                "statusCode",400
            ));
        }
    }

     @PatchMapping("/status")
    public ResponseEntity<?> status(@RequestParam(required = true) Long id){
        try {

                 moodEqualizerService.status(id);
                 return ResponseEntity.ok(Map.of(
                        "status", true, 
                        "message", "status updated", 
                        "statusCode",200
                    ));
        }catch(Exception e){
             return ResponseEntity.status(400).body(Map.of(
                "status", false, 
                "message", e.getMessage(),
                "statusCode",400
            ));
        }
    }

}

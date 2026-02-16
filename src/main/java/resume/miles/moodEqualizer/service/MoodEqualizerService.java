package resume.miles.moodEqualizer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import resume.miles.moodEqualizer.dto.MoodEqualizerDTO;
import resume.miles.moodEqualizer.entity.MoodEqualizerEntitiy;
import resume.miles.moodEqualizer.helper.BannerUploadHelper;
import resume.miles.moodEqualizer.helper.MoodEqualizerHelper;
import resume.miles.moodEqualizer.mapper.MoodEqualizerMapper;
import resume.miles.moodEqualizer.repository.MoodEqualizerRepository;

import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator; 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class MoodEqualizerService {
    @Autowired
    private MoodEqualizerRepository moodEqualizerRepository;

    @Autowired
    private MoodEqualizerHelper moodEqualizerHelper;


    @Autowired
    private BannerUploadHelper bannerUploadHelper;
  
    private final Path uploadDir = Paths.get("src/main/resources/static/uploads/equalizer/files");
    private final Path tempDir = Paths.get("src/main/resources/static/uploads/equalizer/temp");

    public MoodEqualizerService() throws IOException {
        Files.createDirectories(uploadDir);
        Files.createDirectories(tempDir);
    }

   
    public String saveStandardFile(MultipartFile file,Long id,String name) throws IOException {
           String url = moodEqualizerHelper.saveFile(file);
           MoodEqualizerEntitiy moodEqualizerEntitiy = MoodEqualizerEntitiy.builder()
                                                    .awarenessId(id)
                                                    .url(url)
                                                    .type(url)
                                                    .name(name)
                                                    .build();
        moodEqualizerRepository.save(moodEqualizerEntitiy);
        return url;
    }

  
    public Map<String, String> saveChunk(MultipartFile chunk, int chunkIndex, int totalChunks, String fileKey, String originalName) throws IOException {
        
        Path fileTempDir = tempDir.resolve(fileKey);
        if (!Files.exists(fileTempDir)) Files.createDirectories(fileTempDir);

        Path chunkPath = fileTempDir.resolve("chunk_" + chunkIndex);
        Files.copy(chunk.getInputStream(), chunkPath, StandardCopyOption.REPLACE_EXISTING);

   
        try (Stream<Path> files = Files.list(fileTempDir)) {
            if (files.count() == totalChunks) {
                return mergeAndCreateHLSStream(fileKey, totalChunks, originalName);
            }
        }
        
        return null; 
    }

 
    private Map<String, String> mergeAndCreateHLSStream(String fileKey, int totalChunks, String originalName) throws IOException {
        Path fileTempDir = tempDir.resolve(fileKey);
        
     
        String tempMp4Name = "temp_" + System.currentTimeMillis() + "_" + originalName;
        Path tempMp4Path = fileTempDir.resolve(tempMp4Name);

        try (FileOutputStream fos = new FileOutputStream(tempMp4Path.toFile(), true)) {
            for (int i = 0; i < totalChunks; i++) {
                Path chunkPath = fileTempDir.resolve("chunk_" + i);
                Files.copy(chunkPath, fos);
            }
        }

       
        String baseName = originalName.replace(".mp4", "").replace(".mp3", "").replaceAll("\\s+", "");
        String streamFolderName = System.currentTimeMillis() + "_" + baseName;
        Path streamOutputDir = uploadDir.resolve(streamFolderName);
        Files.createDirectories(streamOutputDir);

        Path m3u8Path = streamOutputDir.resolve("index.m3u8");
        Path thumbnailPath = streamOutputDir.resolve("thumbnail.jpg");

       
        String ffmpegExecutable = "ffmpeg"; 
        try {
            DefaultFFMPEGLocator locator = new DefaultFFMPEGLocator();
            ffmpegExecutable = locator.getExecutablePath();
            System.out.println("✅ Found Jave FFmpeg at: " + ffmpegExecutable);
        } catch (Exception e) {
            System.err.println("⚠️ Could not locate Jave FFmpeg, falling back to system default.");
        }

       
        processHLSWithFFmpeg(ffmpegExecutable, tempMp4Path.toString(), m3u8Path.toString());
        
     
        generateThumbnail(ffmpegExecutable, tempMp4Path.toString(), thumbnailPath.toString());

        
        deleteDirectory(fileTempDir);

       
        return Map.of(
            "videoUrl", "/uploads/equalizer/files/" + streamFolderName + "/index.m3u8",
            "thumbnailUrl", "/uploads/equalizer/files/" + streamFolderName + "/thumbnail.jpg",
            "streamFolder", streamFolderName
        );
    }


    private void processHLSWithFFmpeg(String ffmpegExe, String inputPath, String outputPath) {
        try {
            String absInput = Paths.get(inputPath).toAbsolutePath().toString();
            String absOutput = Paths.get(outputPath).toAbsolutePath().toString();

            System.out.println("🎬 Starting HLS Conversion...");

            ProcessBuilder builder = new ProcessBuilder(
                ffmpegExe,
                "-i", absInput,
                "-codec:", "copy",        // Fast copy. If this fails, remove this line to re-encode.
                "-start_number", "0",
                "-hls_time", "10",        // 10 second segments
                "-hls_list_size", "0",    // Keep all segments in the list
                "-f", "hls",
                absOutput
            );

            builder.redirectErrorStream(true);
            Process process = builder.start();

          
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[FFmpeg] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("✅ HLS Conversion Complete: " + outputPath);
            } else {
                System.err.println("❌ FFmpeg Failed with Error Code: " + exitCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void generateThumbnail(String ffmpegExe, String videoPath, String thumbnailPath) {
        try {
            String absInput = Paths.get(videoPath).toAbsolutePath().toString();
            String absOutput = Paths.get(thumbnailPath).toAbsolutePath().toString();

            ProcessBuilder builder = new ProcessBuilder(
                ffmpegExe, 
                "-i", absInput, 
                "-ss", "00:00:01", 
                "-vframes", "1", 
                absOutput
            );
            
            builder.redirectErrorStream(true);
            Process process = builder.start();
            process.waitFor();
            System.out.println("📸 Thumbnail Created");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void deleteDirectory(Path path) throws IOException {
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }


    public boolean saveDatabase(String video,String thumb,Long id,String name){
        MoodEqualizerEntitiy moodEqualizerEntitiy = MoodEqualizerEntitiy.builder()
                                                    .awarenessId(id)
                                                    .url(video)
                                                    .type(thumb)
                                                    .name(name)
                                                    .build();
        moodEqualizerRepository.save(moodEqualizerEntitiy);
        return true;
    }



    public List<MoodEqualizerDTO> findAllData(Long id,Long awid){
        List<MoodEqualizerEntitiy> data = moodEqualizerRepository.findByAllData(id,awid);
        List<MoodEqualizerDTO> dataDTO = data.stream().map(dat->MoodEqualizerMapper.toDTOMapper(dat)).toList();
        return dataDTO;
    }

    public boolean updateBanner(MultipartFile file,Long id) throws IOException{
        MoodEqualizerEntitiy data = moodEqualizerRepository.findById(id).orElseThrow(()->new RuntimeException("id invalid"+" "+id));
        String url = bannerUploadHelper.saveFile(file);
        data.setType(url);
        moodEqualizerRepository.save(data);
        return true;
    }

     @Transactional
     public boolean status(Long id){
       MoodEqualizerEntitiy find = moodEqualizerRepository.findById(id).orElseThrow(()->new RuntimeException("Invalid id"));
       int newStatus = (find.getStatus() == 1) ? 0 : 1;
       find.setStatus(newStatus);
       return true;
    }
}
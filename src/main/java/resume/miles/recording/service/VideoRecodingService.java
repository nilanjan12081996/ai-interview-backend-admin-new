package resume.miles.recording.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.repository.InterviewLinkRepository;
import resume.miles.recording.entity.VideoRecordingEntity;
import resume.miles.recording.repository.VideoRecodingRepository;

@Service
@RequiredArgsConstructor
public class VideoRecodingService {
     private final InterviewLinkRepository interviewLinkRepository;
     private final VideoRecodingRepository videoRecordRepository;
//       public String saveRecording(String token, MultipartFile file) throws IOException {
//
//        // 1️⃣ Find interview link using token
//        InterviewLinkEntity interviewLink = interviewLinkRepository
//                .findByToken(token)
//                .orElseThrow(() -> new RuntimeException("Invalid interview token"));
//
//        // 2️⃣ Check expiry
//        if (interviewLink.getExpiryTime().isBefore(LocalDateTime.now())) {
//            throw new RuntimeException("Interview link expired");
//        }
//
//        // 3️⃣ Prevent duplicate upload
//        boolean exists = videoRecordRepository
//                .existsByInterviewLinkId(interviewLink.getId());
//
//        if (exists) {
//            throw new RuntimeException("Recording already exists for this interview");
//        }
//
//        // 4️⃣ Generate file name
//        String extension = file.getOriginalFilename().endsWith(".mp4") ? ".mp4" : ".webm";
//        String fileName = UUID.randomUUID() + extension;
//
//        Path uploadDir = Paths.get("uploads/");
//        Files.createDirectories(uploadDir);
//
//        Path filePath = uploadDir.resolve(fileName);
//        Files.write(filePath, file.getBytes());
//
//        // String videoUrl = "http://localhost:8085/uploads/" + fileName;
//          String videoUrl = "/uploads/" + fileName;
//
//        // 5️⃣ Save into video_record table
//        VideoRecordingEntity record = new VideoRecordingEntity();
//        record.setInterviewLinkId(interviewLink.getId());
//        record.setVideoLink(videoUrl);
//        record.setStatus(1);
//        record.setCreatedAt(LocalDateTime.now());
//
//        videoRecordRepository.save(record);
//
//        return videoUrl;
//    }

    public String saveRecording(String token, MultipartFile file) throws IOException {

        // 1️⃣ Find interview link using token
        InterviewLinkEntity interviewLink = interviewLinkRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid interview token"));

        // 2️⃣ Check expiry
        if (interviewLink.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Interview link expired");
        }

        // 3️⃣ Generate a consistent file name based on the token
        // This ensures all chunks from this interview go to the exact same file
        String fileName = token + ".webm";

        Path uploadDir = Paths.get("uploads/");
        if(Files.notExists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path filePath = uploadDir.resolve(fileName);

        // 4️⃣ Append bytes to the file instead of overwriting
        Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        String videoUrl = "/uploads/" + fileName;

        // 5️⃣ Only save into video_record table if it doesn't already exist
        // This prevents creating duplicate database rows for every 1-minute chunk
        if (!videoRecordRepository.existsByInterviewLinkId(interviewLink.getId())) {
            VideoRecordingEntity record = new VideoRecordingEntity();
            record.setInterviewLinkId(interviewLink.getId());
            record.setVideoLink(videoUrl);
            record.setStatus(1);
            record.setCreatedAt(LocalDateTime.now());

            videoRecordRepository.save(record);
        }

        return videoUrl;
    }

}

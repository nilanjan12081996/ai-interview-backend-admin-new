package resume.miles.recording.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.recording.entity.VideoRecordingEntity;

@Repository
public interface VideoRecodingRepository extends JpaRepository<VideoRecordingEntity,Long>{
    boolean existsByInterviewLinkId(Long interviewLinkId);
    Optional<VideoRecordingEntity> findByInterviewLinkId(Long interviewLinkId);
}

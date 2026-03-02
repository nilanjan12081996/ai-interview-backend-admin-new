package resume.miles.transcription.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.transcription.entity.TranscriptionEntity;

@Repository
public interface TransciptionRepository extends JpaRepository<TranscriptionEntity,Long>{

     List<TranscriptionEntity> findAllByInterviewLinkId(Long interviewLinkId);

       Optional<TranscriptionEntity> 
    findTopByInterviewLinkIdOrderByCreatedAtDesc(Long interviewLinkId);
}

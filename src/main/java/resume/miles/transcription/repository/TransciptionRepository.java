package resume.miles.transcription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.transcription.entity.TranscriptionEntity;

@Repository
public interface TransciptionRepository extends JpaRepository<TranscriptionEntity,Long>{

    
}

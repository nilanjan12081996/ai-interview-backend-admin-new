package resume.miles.moodEqualizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import resume.miles.moodEqualizer.entity.MoodEqualizerEntitiy;


@Repository
public interface MoodEqualizerRepository extends JpaRepository<MoodEqualizerEntitiy,Long> {

 @Query("SELECT s FROM MoodEqualizerEntitiy as s WHERE (:id IS NULL OR s.id = :id) AND (:awid IS NULL OR s.awarenessId = :awid) ORDER BY s.createdAt DESC")
 List<MoodEqualizerEntitiy>findByAllData(@Param("id") Long id,@Param("awid") Long awid);
    
}
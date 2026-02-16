package resume.miles.moodEqualizer.mapper;

import resume.miles.moodEqualizer.dto.MoodEqualizerDTO;
import resume.miles.moodEqualizer.entity.MoodEqualizerEntitiy;

public class MoodEqualizerMapper {
    public static MoodEqualizerDTO toDTOMapper(MoodEqualizerEntitiy moodEqualizerEntitiy){
                    MoodEqualizerDTO data = MoodEqualizerDTO.builder()
                                            .id(moodEqualizerEntitiy.getId())
                                            .awarenessId(moodEqualizerEntitiy.getAwarenessId())
                                            .url(moodEqualizerEntitiy.getUrl())
                                            .name(moodEqualizerEntitiy.getName())
                                            .status(moodEqualizerEntitiy.getStatus())
                                            .type(moodEqualizerEntitiy.getType())
                                            .createdAt(moodEqualizerEntitiy.getCreatedAt())
                                            .updatedAt(moodEqualizerEntitiy.getUpdatedAt())
                                            .build();

        return data;
    }
}

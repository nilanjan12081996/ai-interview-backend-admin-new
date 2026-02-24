package resume.miles.recording.mapper;

import resume.miles.recording.dto.VideoRecordingDto;
import resume.miles.recording.entity.VideoRecordingEntity;

public class VideoRecodingMapper {
    public static VideoRecordingDto toDto(VideoRecordingEntity entity) {
        if (entity == null) {
            return null;
        }

        return VideoRecordingDto.builder()
                .id(entity.getId())
                .interviewLinkId(entity.getInterviewLinkId())
                .videoLink(entity.getVideoLink())
                .build();
    }

    public static VideoRecordingEntity toEntity(VideoRecordingDto dto) {
        if (dto == null) {
            return null;
        }

        return VideoRecordingEntity.builder()
                .id(dto.getId())
                .interviewLinkId(dto.getInterviewLinkId())
                .videoLink(dto.getVideoLink())
                .build();
    }

}

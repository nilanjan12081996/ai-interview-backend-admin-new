package resume.miles.interview.mapper;

import resume.miles.interview.dto.InterviewDto;
import resume.miles.interview.entity.InterviewEntity;

public class InterviewMapper {
    public static InterviewEntity toEntity(InterviewDto dto) {

        if (dto == null) {
            return null;
        }

        return InterviewEntity.builder()
                .jobId(dto.getJobId())
                .candidateName(dto.getCandidateName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .resumeLink(dto.getResumeLink())
                .isCoding(dto.getIsCoding())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .interviewDate(dto.getInterviewDate())
                .status(dto.getStatus() != null ? dto.getStatus() : 1)
                .build();
    }
    public static InterviewDto toDTO(InterviewEntity entity) {

        if (entity == null) {
            return null;
        }

        return InterviewDto.builder()
                .id(entity.getId())
                .jobId(entity.getJobId())
                .candidateName(entity.getCandidateName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .resumeLink(entity.getResumeLink())
                .isCoding(entity.getIsCoding())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .interviewDate(entity.getInterviewDate())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

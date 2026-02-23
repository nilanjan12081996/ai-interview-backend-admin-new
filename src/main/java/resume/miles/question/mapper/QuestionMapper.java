package resume.miles.question.mapper;

import resume.miles.question.dto.QuestionResponseDTO;
import resume.miles.question.entity.QuestionEntity;

public class QuestionMapper {

    public static QuestionResponseDTO toDTO(QuestionEntity entity) {
        return QuestionResponseDTO.builder()
                .id(entity.getId())
                .question(entity.getQuestions())
                .build();
    }
}
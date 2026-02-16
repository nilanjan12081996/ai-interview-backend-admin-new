package resume.miles.questions.mapper;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import resume.miles.questions.dto.AnswerDTO;
import resume.miles.questions.dto.QuestionAnswerDTO;
import resume.miles.questions.dto.QuestionDTO;
import resume.miles.questions.entity.Answer;
import resume.miles.questions.entity.Question;

public class QuestionAnswerMappper {
     public static Answer toAnswerToSave(AnswerDTO answerDTO,Long id){
        Answer ans = Answer.builder()
                     .id(answerDTO.getId())
                     .answer(answerDTO.getAnswer())
                     .questionId(id)
                     .point(answerDTO.getPoint())
                     .build();
        return ans;
     }
     public static AnswerDTO toAnswerDTO(Answer answer){
        AnswerDTO ans = AnswerDTO.builder()
                     .id(answer.getId())
                     .answer(answer.getAnswer())
                     .questionId(answer.getQuestionId())
                     .status(answer.getStatus())
                     .point(answer.getPoint())
                     .createdAt(answer.getCreatedAt())
                     .updatedAt(answer.getUpdatedAt())
                     .build();
        return ans;
     }
     public static Question toQuestion( QuestionAnswerDTO question){
        Question qs = Question.builder()
                     .id(question.getId())
                     .question(question.getQuestion())
                     .status(1)
                     .build();
        return qs;
     }
     public static QuestionDTO toQuestionDTO( Question question){
        QuestionDTO qs = QuestionDTO.builder()
                     .id(question.getId())
                     .question(question.getQuestion())
                     .status(question.getStatus())
                     .createdAt(question.getCreatedAt())
                     .updatedAt(question.getUpdatedAt())
                     .answer(question.getAnswers().stream().map(ans->toAnswerDTO(ans)).collect(Collectors.toList()))
                     .build();
        return qs;
     }
      
}

package resume.miles.questions.service;

import java.util.List;

import resume.miles.questions.dto.QuestionAnswerDTO;
import resume.miles.questions.dto.QuestionDTO;

public interface QuestionAnswerService {
    QuestionAnswerDTO insertQuestionAns(QuestionAnswerDTO questionAns);
    List<QuestionDTO> list(Long id);
    boolean delete(Long id);
    boolean ansStatus(Long id);
    boolean questionStatus(Long id);
}

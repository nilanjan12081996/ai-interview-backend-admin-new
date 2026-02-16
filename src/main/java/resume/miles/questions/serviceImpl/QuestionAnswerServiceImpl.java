package resume.miles.questions.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import resume.miles.questions.dto.AnswerDTO;
import resume.miles.questions.dto.QuestionAnswerDTO;
import resume.miles.questions.dto.QuestionDTO;
import resume.miles.questions.entity.Answer;
import resume.miles.questions.entity.Question;
import resume.miles.questions.mapper.QuestionAnswerMappper;
import resume.miles.questions.repository.AnswerRepository;
import resume.miles.questions.repository.QuestionRepository;
import resume.miles.questions.repository.specification.QuestionSpecification;
import resume.miles.questions.service.QuestionAnswerService;

@Service
public class QuestionAnswerServiceImpl implements QuestionAnswerService{

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Override
    @Transactional
    public QuestionAnswerDTO insertQuestionAns(QuestionAnswerDTO questionAns){
        if(questionAns.getId() == null){
            Question question = QuestionAnswerMappper.toQuestion(questionAns);
            Question questionSet = questionRepository.save(question);
            
            for(AnswerDTO ans : questionAns.getAnswer()){
                Answer answToSave = QuestionAnswerMappper.toAnswerToSave(ans,questionSet.getId());
                Answer saveAns = answerRepository.save(answToSave);
            }
        }else{
            Question question = QuestionAnswerMappper.toQuestion(questionAns);
            Question findQuestion = questionRepository.findById(question.getId()).orElseThrow(()->new RuntimeException("Question not found with id: " + question.getId()));
            Question updateQuestion = questionRepository.save(question);
            for(AnswerDTO ans : questionAns.getAnswer()){
                Answer answToSave = QuestionAnswerMappper.toAnswerToSave(ans,question.getId());
                if(answToSave.getId() == null){
                    Answer saveAns = answerRepository.save(answToSave);
                }else{
                        Answer findAns = answerRepository.findById(answToSave.getId()).orElseThrow(()->new RuntimeException("Answer not found with id: " + answToSave.getId()));
                        Answer updateAns = answerRepository.save(answToSave);
                }
            
            }
        }
        
        return questionAns;
    }

    @Override
    public List<QuestionDTO> list(Long id){
        Specification<Question> spc = null;
        spc = spc == null ? Specification.where(QuestionSpecification.joinTable(id)) : spc.and(QuestionSpecification.joinTable(id));

        List<Question>  question =spc == null ? questionRepository.findAll():questionRepository.findAll(spc);

        List<QuestionDTO> questionDTOs = question.stream()
            .map(q -> QuestionAnswerMappper.toQuestionDTO(q)) 
            .collect(Collectors.toList());

        return questionDTOs;
    }

    @Override
    public boolean delete(Long id){
        if (!answerRepository.existsById(id)) {
            throw new RuntimeException("Ans not found with id: " + id);
        }
        answerRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean ansStatus(Long id){
         Answer findAns = answerRepository.findById(id).orElseThrow(()->new RuntimeException("Answer not found with id: " + id));
         Integer newStatus = (findAns.getStatus() == 1) ? 0 : 1;
         findAns.setStatus(newStatus);
         answerRepository.save(findAns);
         return true;
    }

    @Override
    public boolean questionStatus(Long id){
         Question findQs = questionRepository.findById(id).orElseThrow(()->new RuntimeException("Question not found with id: " + id));
         Integer newStatus = (findQs.getStatus() == 1) ? 0 : 1;
         findQs.setStatus(newStatus);
         questionRepository.save(findQs);
         return true;
    }
}

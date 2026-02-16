package resume.miles.questionAwarenessmap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import resume.miles.awarness.repository.AwarnessRepository;
import resume.miles.questionAwarenessmap.dto.AwarenessQuestionMapDTO;

import resume.miles.questionAwarenessmap.entity.AwarenessQuestionMap;
import resume.miles.questionAwarenessmap.mapper.AwarnessQuestionMapper;
import resume.miles.questionAwarenessmap.repository.AwarnessQuestionMapRepository;
import resume.miles.questionAwarenessmap.repository.specification.QuestionAwarenessMapSpecification;
import resume.miles.questions.repository.QuestionRepository;

@Service
public class AwarenessQuestionService {

    @Autowired
    private AwarnessQuestionMapRepository awarnessQuestionMapRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AwarnessRepository awarnessRepository;


    public boolean mapped(AwarenessQuestionMapDTO awarenessQuestionMapDTO){
            AwarenessQuestionMap creatableData = AwarnessQuestionMapper.toEntityMapper(awarenessQuestionMapDTO);
            // Long count = awarnessQuestionMapRepository.countByAwarenessId(creatableData.getAwarenessId());
            // if(count>=5){
            //     throw new RuntimeException("Max number of question already mapped");
            // }
            Long countTotal = awarnessQuestionMapRepository.countByQuestionIdAndAwarenessId(creatableData.getQuestionId(),creatableData.getAwarenessId());
            if(countTotal>=1){
                 throw new RuntimeException("The question and the awareness already mapped");
            }
            Long qid = questionRepository.countById(creatableData.getQuestionId());
            if(qid ==0){
                 throw new RuntimeException("Invalid question id");
            }
            Long aid = awarnessRepository.countById(creatableData.getAwarenessId());
            if(aid==0){
                throw new RuntimeException("Invalid awareness id");
            }
            AwarenessQuestionMap save = awarnessQuestionMapRepository.save(creatableData);
            return true;
    }

    public boolean delete(Long id){
        if (awarnessQuestionMapRepository.existsById(id)) {
            awarnessQuestionMapRepository.deleteById(id);
            return true; 
        }
       throw new RuntimeException("Invalid id");
    }
    
    @Transactional
    public boolean status(Long id){
       AwarenessQuestionMap find = awarnessQuestionMapRepository.findById(id).orElseThrow(()->new RuntimeException("Invalid id"));
       int newStatus = (find.getStatus() == 1) ? 0 : 1;
       find.setStatus(newStatus);
       return true;
    }


    public List<AwarenessQuestionMapDTO> getQuestions(Long awarenessId) {
    
           
            Specification<AwarenessQuestionMap> spec = Specification
                .where(QuestionAwarenessMapSpecification.withQuestionData())
                .and((root, query, cb) -> cb.equal(root.get("awarenessId"), awarenessId)); 

            List<AwarenessQuestionMap> maps = awarnessQuestionMapRepository.findAll(spec);

            List<AwarenessQuestionMapDTO> mapsDto = AwarnessQuestionMapper.toDtoList(maps);
            return mapsDto;
    }
    
}

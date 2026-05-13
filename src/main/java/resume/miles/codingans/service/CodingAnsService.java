package resume.miles.codingans.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resume.miles.codingans.dto.CodingAnsDto;
import resume.miles.codingans.entity.CodingAnsEntity;
import resume.miles.codingans.mapper.CodingAnsMapper;
import resume.miles.codingans.repository.CodingAnsRepository;
import resume.miles.codingquestion.entity.CodingEntity;
import resume.miles.codingquestion.repository.CodingRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodingAnsService {
    private final CodingRepository codingRepository;
    private final CodingAnsRepository codingAnsRepository;

    @Transactional
    public CodingAnsDto save(CodingAnsDto codingAnsDto) {
          CodingAnsEntity saveData;
          CodingEntity codingEntity =  codingRepository.findById(codingAnsDto.getQuestionId()).orElseThrow(()->new RuntimeException("CodingEntity not found"));
          if(codingAnsDto.getId()==null){
              CodingAnsEntity codingAnsEntity = CodingAnsMapper.toEntity(codingAnsDto);
              codingAnsEntity.setStatus(1);
              codingAnsEntity.setIsDeleted(0);
              saveData = codingAnsRepository.save(codingAnsEntity);
          }else{
              Optional<CodingAnsEntity> codingAnsEntityFound =  codingAnsRepository.findById(codingAnsDto.getId());
              if(codingAnsEntityFound.isEmpty()){
                  throw new RuntimeException("CodingAns not found");

              }
              CodingAnsEntity codingAnsEntityData = codingAnsEntityFound.get();
              codingAnsEntityData.setAns(codingAnsDto.getAns());

              saveData = codingAnsRepository.save(codingAnsEntityData);
          }

          return CodingAnsMapper.toDto(saveData);
    }

    @Transactional
    public CodingAnsDto list(Long id) {
        Optional<CodingAnsEntity> codingAnsEntityFound =  codingAnsRepository.findById(id);
        if(codingAnsEntityFound.isEmpty()){
            throw new RuntimeException("CodingAns not found");

        }
        return CodingAnsMapper.toDto(codingAnsEntityFound.get());
    }
}

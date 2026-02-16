package resume.miles.supportcategory.service;

import java.io.IOError;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import resume.miles.supportcategory.dto.SupportCategoryDto;
import resume.miles.supportcategory.entity.SupportCategoryEntity;
import resume.miles.supportcategory.helper.FileUploadHelper;
import resume.miles.supportcategory.mapper.SupportCategoryMapper;
import resume.miles.supportcategory.repository.SubcategoryRepository;

@Service
public class SubcategoryService {

    @Autowired
    private FileUploadHelper fileUploadHelper;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Transactional
    public SupportCategoryDto saveOrUpdate(SupportCategoryDto supportCategoryDto){
            SupportCategoryEntity supportCategoryEntityForSaveDetails ;
            if(supportCategoryDto.getId() == null){
                SupportCategoryEntity supportCategoryEntityForSave = SupportCategoryMapper.toEntity(supportCategoryDto);
                 supportCategoryEntityForSaveDetails = subcategoryRepository.save(supportCategoryEntityForSave);
            }else{
                supportCategoryEntityForSaveDetails = subcategoryRepository.findById(supportCategoryDto.getId()).orElseThrow(()-> new RuntimeException("invalid id   "+ supportCategoryDto.getId()));
                supportCategoryEntityForSaveDetails.setName(supportCategoryDto.getName());
            }
            SupportCategoryDto supportCategoryDtoResponse = SupportCategoryMapper.toDto(supportCategoryEntityForSaveDetails);

            return supportCategoryDtoResponse;
    }


    public List<SupportCategoryDto> list(Long id){
        List<SupportCategoryEntity> dataOfEntity =   subcategoryRepository.findByIdOrAll(id);

        List<SupportCategoryDto> dataOfDTO = dataOfEntity.stream().map(dat->SupportCategoryMapper.toDto(dat)).collect(Collectors.toList());
        return dataOfDTO;
    }


     public List<SupportCategoryDto> childList(Long pid,Long id){
        List<SupportCategoryEntity> dataOfEntity =   subcategoryRepository.findByIdOrAllByCategory(pid,id);

        List<SupportCategoryDto> dataOfDTO = dataOfEntity.stream().map(dat->SupportCategoryMapper.toDto(dat)).collect(Collectors.toList());
        return dataOfDTO;
    }

    @Transactional
    public SupportCategoryDto status(Long id){
        SupportCategoryEntity dataOfEntity =   subcategoryRepository.findById(id).orElseThrow(()->new RuntimeException("id invalid " + id));
        Integer data = (dataOfEntity.getStatus() == 0)?1:0;
        dataOfEntity.setStatus(data);
        SupportCategoryDto dataResponse = SupportCategoryMapper.toDto(dataOfEntity);
        return dataResponse;
    }

    @Transactional
    public SupportCategoryDto file(MultipartFile file, Long id) throws IOException{
          SupportCategoryEntity data = subcategoryRepository.findById(id).orElseThrow(()->new RuntimeException("id invalid " + id));
          String url =  fileUploadHelper.saveFile(file);
          data.setImage(url);
          SupportCategoryDto dataResponse = SupportCategoryMapper.toDto(data);
          return dataResponse;
    }
 
 
    @Transactional
    public List<SupportCategoryDto> getSubcategoryWithoutParent(){
       List<SupportCategoryEntity> entity=subcategoryRepository.findByParentIdNot(0L);
        List<SupportCategoryDto> dataOfDTO = entity.stream().map(dat->SupportCategoryMapper.toDto(dat)).collect(Collectors.toList());
        return dataOfDTO;
    }
     

}

package resume.miles.awarness.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import resume.miles.awarness.dto.AwarenessDTO;
import resume.miles.awarness.dto.ResponseAwarenessDTO;
import resume.miles.awarness.dto.UpdateAwarenessDTO;
import resume.miles.awarness.entiry.AwarenessEntity;
import resume.miles.awarness.helper.AwarnessFileUploadHelper;
import resume.miles.awarness.mapper.AwarnessMapper;
import resume.miles.awarness.repository.AwarnessRepository;
import resume.miles.sidebar.repository.SubSidebarRepository;

@Service
public class AwarnessService {

    @Autowired
    private AwarnessRepository awarnessRepository;

    @Autowired
    private SubSidebarRepository subSidebarRepository;

    @Autowired
    private AwarnessFileUploadHelper awarnessFileUploadHelper;

    @Value("${urls.baseUrl}")
    private String baseUrl;
    
    public AwarenessDTO saveData(AwarenessDTO awarenessDTO){
        if (awarenessDTO.getSubsidebarId() != null) {
            boolean exists = subSidebarRepository.existsById(awarenessDTO.getSubsidebarId());
            if (!exists) {
                throw new RuntimeException("SubSidebar ID " + awarenessDTO.getSubsidebarId() + " does not exist.");
            }
        }
        AwarenessEntity toSaveData =  AwarnessMapper.toEntityAwareness(awarenessDTO);
        AwarenessEntity saveData = awarnessRepository.save(toSaveData);
        AwarenessDTO responseData = AwarnessMapper.toAwarenessDTO(saveData);
        return responseData;
    }

    public AwarenessDTO updateData(UpdateAwarenessDTO awarenessDTO){
        AwarenessEntity entity = awarnessRepository.findById(awarenessDTO.getId())
            .orElseThrow(() -> new RuntimeException("Awareness ID " + awarenessDTO.getId() + " not found"));
            if (awarenessDTO.getAwarenessName() != null) {
                entity.setAwarenessName(awarenessDTO.getAwarenessName());
            }

            if (awarenessDTO.getDescription() != null) {
                entity.setDescription(awarenessDTO.getDescription());
            }
            if (awarenessDTO.getColorCode() != null) {
                entity.setColorCode(awarenessDTO.getColorCode());
            }

           
            
            AwarenessEntity updatedEntity = awarnessRepository.save(entity);

            return AwarnessMapper.toAwarenessDTO(updatedEntity);

    }

    public boolean uploadImage(Long id, MultipartFile file) throws IOException{
        AwarenessEntity entity = awarnessRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Awareness ID " + id + " not found"));


        if (file.isEmpty()) {
            throw new RuntimeException("File cannot be empty");
        }

        String url =  awarnessFileUploadHelper.saveFile(file);
        entity.setImage(url);
        awarnessRepository.save(entity);
        return true;
    }


    public List<ResponseAwarenessDTO> findDataWithSidebarId(Long id){
            List<AwarenessEntity> data =  awarnessRepository.findBySubsidebarId(id);
            List<ResponseAwarenessDTO> dtoData = data.stream().map(dat->AwarnessMapper.toResponseAwareness(dat,baseUrl)).collect(Collectors.toList());
            return dtoData;
    }

     public ResponseAwarenessDTO findData(Long id){
            AwarenessEntity data =  awarnessRepository.findById(id).orElseThrow(()-> new RuntimeException("data not found for this id"+ id));
            ResponseAwarenessDTO dtoData = AwarnessMapper.toResponseAwareness(data,baseUrl);
            return dtoData;
    }

    public AwarenessDTO toggleStatus(Long id){
        AwarenessEntity entity=awarnessRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Awareness ID " + id + " not found"));
        Integer currentStatus=entity.getStatus();
        entity.setStatus((currentStatus!=null && currentStatus==1)? 0:1);
        AwarenessEntity savedEntity = awarnessRepository.save(entity);
      return AwarnessMapper.toAwarenessDTO(savedEntity);
        
    }
}

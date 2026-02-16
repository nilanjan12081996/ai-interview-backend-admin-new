package resume.miles.supportcategorycharges.service;


import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import resume.miles.supportcategory.entity.SupportCategoryEntity;
import resume.miles.supportcategory.repository.SubcategoryRepository;
import resume.miles.supportcategorycharges.dto.SupportCategoryChargesDto;
import resume.miles.supportcategorycharges.entity.SupportCategoryChargesEntity;
import resume.miles.supportcategorycharges.mapper.SupportCategoryChargesMapper;
import resume.miles.supportcategorycharges.repository.SupportCategoryChargesRepository;

@Service
@RequiredArgsConstructor
public class SupportCategoryChargeService {
  
    private final SupportCategoryChargesRepository repo;
    private final SubcategoryRepository supportRepo;
    private final SupportCategoryChargesMapper mapper;
    //  private final SupportCategoryChargesMapper mapper = new SupportCategoryChargesMapper();
    @Transactional
    public SupportCategoryChargesDto saveCharges(SupportCategoryChargesDto entity){
        System.out.println("getSupport_category_id"+entity);
       if (entity.getSupport_category()==null) {
        throw new IllegalArgumentException("Support category ID cannot be null");
       }
       if(entity.getCharge()==null){
          throw new IllegalArgumentException("Charge cannot be null");
       }
        if (entity.getStatus() == null) {
            entity.setStatus(1); // default active
        }
        SupportCategoryChargesEntity supportCategoryChargesEntitydata ;
        SupportCategoryEntity support =  supportRepo.findById(entity.getSupport_category()).orElseThrow(()-> new IllegalArgumentException("category id not found"));
        if (entity.getId() != null) {
            supportCategoryChargesEntitydata = repo.findById(entity.getId()).orElseThrow(()-> new IllegalArgumentException("i not found"));
            if(entity.getCharge() != null){
                supportCategoryChargesEntitydata.setCharge(entity.getCharge());
            }
            repo.save(supportCategoryChargesEntitydata);
        }else{

              SupportCategoryChargesEntity entityData=mapper.toEntity(entity);
              Optional<SupportCategoryChargesEntity> findData = repo.findBySupportCategory(entityData.getSupportCategory());
              if (findData.isPresent()) {
                   
                    supportCategoryChargesEntitydata = findData.get();
                    supportCategoryChargesEntitydata.setCharge(entityData.getCharge());
                    supportCategoryChargesEntitydata.setStatus(entityData.getStatus());
                } else {
                    
                    supportCategoryChargesEntitydata = entityData;
                }

                supportCategoryChargesEntitydata = repo.save(supportCategoryChargesEntitydata);
              


        }
        SupportCategoryChargesDto dataDto = mapper.toDto(supportCategoryChargesEntitydata);
        return dataDto;
       
       
    }

    @Transactional
    public SupportCategoryChargesDto getCharges(Long supportCategoryId){
           SupportCategoryChargesEntity entity =
            repo.findBySupportCategory(supportCategoryId)
                .orElseThrow(() ->
                        new RuntimeException("Charges not found for this support category")
                );

    return mapper.toDto(entity);
    
    }

}

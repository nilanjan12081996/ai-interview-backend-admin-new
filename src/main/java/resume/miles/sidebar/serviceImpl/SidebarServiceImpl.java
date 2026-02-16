package resume.miles.sidebar.serviceImpl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import resume.miles.sidebar.dto.MasterOnlySidebarDTO;
import resume.miles.sidebar.dto.MasterSidebarDto;
import resume.miles.sidebar.dto.SubSidebarDto;
import resume.miles.sidebar.entity.MasterSidebarEntity;
import resume.miles.sidebar.entity.SubSidebarEntity;
import resume.miles.sidebar.mapper.SidebarMapper;
import resume.miles.sidebar.repository.MasterSidebarRepository;
import resume.miles.sidebar.repository.SubSidebarRepository;

import resume.miles.sidebar.service.SidebarService;

@Service
public class SidebarServiceImpl implements SidebarService{
        @Autowired
        private MasterSidebarRepository masterSidebarRepository;

        @Autowired
        private SubSidebarRepository subSidebarRepository;
        
        @Override
        public List<MasterSidebarDto> getList(Long id){
            Specification<MasterSidebarEntity> spec = null;
            // spec = spec == null ? Specification.where(MasterSidebarSpecification.joinTable(id)) : spec.and(MasterSidebarSpecification.joinTable(id));
            // List<MasterSidebarEntity> dataEntity = spec == null ? masterSidebarRepository.findAll():masterSidebarRepository.findAll(spec);
            // List<MasterSidebarDto> data =  dataEntity.stream().map(dat->SidebarMapper.toMasterSidebarDto(dat)).collect(Collectors.toList());

            return masterSidebarRepository.findCustomSidebarDetails(id);
            
            // return data;
        }
        @Override
        public List<MasterOnlySidebarDTO> getListMaster(Long id){
            List<MasterSidebarEntity> masterData = masterSidebarRepository.findByDataOptionalId(id);
            List<MasterOnlySidebarDTO> data = masterData.stream().map((dat)->SidebarMapper.toMasterSidebarOnlyDto(dat)).collect(Collectors.toList());
           
            return data;
        }

        public List<SubSidebarDto> getListSub(Long id){
            List<SubSidebarEntity> masterData = subSidebarRepository.findByDataOptionalId(id);
            List<SubSidebarDto> data = masterData.stream().map(dat->SidebarMapper.toSubsidebarDto(dat)).collect(Collectors.toList());
            return data;
        }


        public boolean getListMasterUpdate(String Data,Long id){
            MasterSidebarEntity masterSidebarEntity = masterSidebarRepository.findById(id).orElseThrow(()->new RuntimeException("no master sidebar found"+" "+id));
            masterSidebarEntity.setSidebarName(Data);
            masterSidebarRepository.save(masterSidebarEntity);
            return true;
        }

        public boolean getListSideUpdate(String Data,Long id){
            SubSidebarEntity subSidebarEntity = subSidebarRepository.findById(id).orElseThrow(()->new RuntimeException("no sub sidebar found"+" "+id));
            subSidebarEntity.setSubSidebarName(Data);
            subSidebarRepository.save(subSidebarEntity);
            return true;
        }
}

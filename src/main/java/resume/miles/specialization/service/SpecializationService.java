package resume.miles.specialization.service;

import org.springframework.stereotype.Service;
import resume.miles.specialization.dto.SpecializationDTO;
import resume.miles.specialization.entity.SpecializationEntity;
import resume.miles.specialization.mapper.SpecializationMapper;
import resume.miles.specialization.repository.SpecializationRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecializationService {

    private final SpecializationRepository repository;

    public SpecializationService(SpecializationRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public SpecializationDTO create(SpecializationDTO dto) {
        SpecializationEntity entity = SpecializationMapper.toEntity(dto);
        entity.setId(null); 
        SpecializationEntity saved = repository.save(entity);
        return SpecializationMapper.toDTO(saved);
    }

    // UPDATE
    public SpecializationDTO update(SpecializationDTO dto) {
        SpecializationEntity existing = repository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Specialization not found with id: " + dto.getId()));

        existing.setName(dto.getName());
        existing.setDes(dto.getDes());
        // We do not update status here usually, unless explicitly required
        
        SpecializationEntity saved = repository.save(existing);
        return SpecializationMapper.toDTO(saved);
    }

    // TOGGLE STATUS (Auto Switch)
    public SpecializationDTO toggleStatus(Long id) {
        SpecializationEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specialization not found with id: " + id));
        
        // If 1 then 0, else 1
        int newStatus = (existing.getStatus() != null && existing.getStatus() == 1) ? 0 : 1;
        existing.setStatus(newStatus);
        
        SpecializationEntity saved = repository.save(existing);
        return SpecializationMapper.toDTO(saved);
    }

    // GET
    public List<SpecializationDTO> getSpecializations(Long id) {
        if (id != null) {
            return repository.findById(id)
                    .map(SpecializationMapper::toDTO)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        } else {
            return repository.findAll().stream()
                    .map(SpecializationMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }
}
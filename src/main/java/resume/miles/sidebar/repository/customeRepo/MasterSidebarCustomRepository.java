package resume.miles.sidebar.repository.customeRepo;

import java.util.List;

import resume.miles.sidebar.dto.MasterSidebarDto;

public interface MasterSidebarCustomRepository {
    List<MasterSidebarDto> findCustomSidebarDetails(Long id);
}

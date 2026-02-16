package resume.miles.sidebar.service;

import java.util.List;

import resume.miles.sidebar.dto.MasterOnlySidebarDTO;
import resume.miles.sidebar.dto.MasterSidebarDto;

public interface SidebarService {

  List<MasterSidebarDto> getList(Long id);
  List<MasterOnlySidebarDTO> getListMaster(Long id);
}

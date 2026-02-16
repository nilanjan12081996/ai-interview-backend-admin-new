package resume.miles.sidebar.mapper;


import java.util.Comparator;
import java.util.stream.Collectors;

import resume.miles.sidebar.dto.MasterOnlySidebarDTO;
import resume.miles.sidebar.dto.MasterSidebarDto;
import resume.miles.sidebar.dto.SubSidebarDto;
import resume.miles.sidebar.entity.MasterSidebarEntity;
import resume.miles.sidebar.entity.SubSidebarEntity;


public class SidebarMapper {
    public static SubSidebarDto toSubsidebarDto(SubSidebarEntity subSidebarEntity){
        SubSidebarDto sidebar = SubSidebarDto.builder()
                                .id(subSidebarEntity.getId())
                                .subSidebarName(subSidebarEntity.getSubSidebarName())
                                .subSidebarShortName(subSidebarEntity.getSubSidebarShortName())
                                .status(subSidebarEntity.getStatus())
                                .masterSidebarId(subSidebarEntity.getMasterSidebar())
                                .createdAt(subSidebarEntity.getCreatedAt())
                                .updatedAt(subSidebarEntity.getUpdatedAt())
                                .build();

        return sidebar;
    }
     public static MasterSidebarDto toMasterSidebarDto(MasterSidebarEntity MasterSidebar){
        MasterSidebarDto sidebar = MasterSidebarDto.builder()
                                .id(MasterSidebar.getId())
                                .sidebarName(MasterSidebar.getSidebarName())
                                .sidebarShortName(MasterSidebar.getSidebarShortName())
                                .subsidebar(
                                    MasterSidebar.getSubSidebarEntity() == null ? null :
                                    MasterSidebar.getSubSidebarEntity().stream()
                                        .sorted(Comparator.comparing(SubSidebarEntity::getId))  
                                        .map(entity -> toSubsidebarDto(entity))
                                        .collect(Collectors.toList())
                                )
                                .status(MasterSidebar.getStatus())
                                .createdAt(MasterSidebar.getCreatedAt())
                                .updatedAt(MasterSidebar.getUpdatedAt())
                                .build();

        return sidebar;
    }
    

      public static MasterOnlySidebarDTO toMasterSidebarOnlyDto(MasterSidebarEntity MasterSidebar){
        MasterOnlySidebarDTO sidebar = MasterOnlySidebarDTO.builder()
                                .id(MasterSidebar.getId())
                                .sidebarName(MasterSidebar.getSidebarName())
                                .sidebarShortName(MasterSidebar.getSidebarShortName())
                                .status(MasterSidebar.getStatus())
                                .createdAt(MasterSidebar.getCreatedAt())
                                .updatedAt(MasterSidebar.getUpdatedAt())
                                .build();

        return sidebar;
    }

}

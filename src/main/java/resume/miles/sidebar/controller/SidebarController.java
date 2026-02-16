package resume.miles.sidebar.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import resume.miles.sidebar.dto.MasterOnlySidebarDTO;
import resume.miles.sidebar.dto.MasterSidebarDto;
import resume.miles.sidebar.dto.SubSidebarDto;
import resume.miles.sidebar.serviceImpl.SidebarServiceImpl;

@RestController
@RequestMapping("/api/goodmood/sidebar")
public class SidebarController {

    @Autowired
    private SidebarServiceImpl sidebarServiceImpl;

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(required = false) Long id){
            try{

                List<MasterSidebarDto> data =  sidebarServiceImpl.getList(id);
                return ResponseEntity.status(200).body(Map.of(
                    "message", "List found",
                    "status", true,
                    "statusCode", 200,
                    "data",data
                ));
            }catch(RuntimeException e){
                return ResponseEntity.status(422).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 422,
                    "errors", e.getStackTrace()
                ));
            }catch(Exception e){
                return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 400,
                    "errors", e.getStackTrace()
                ));
            }
    }
    @GetMapping("/main-sidebar-list")
    public ResponseEntity<?> mainSidebar(@RequestParam(required = false) Long id){
            try{

                List<MasterOnlySidebarDTO> data = sidebarServiceImpl.getListMaster(id);
                return ResponseEntity.status(200).body(Map.of(
                    "message", "List found",
                    "status", true,
                    "statusCode", 200,
                    "data",data
              
                ));
            }catch(RuntimeException e){
                return ResponseEntity.status(422).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 422,
                    "errors", e.getStackTrace()
                ));
            }catch(Exception e){
                return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 400,
                    "errors", e.getStackTrace()
                ));
            }
    }


    @GetMapping("/sub-sidebar-list")
    public ResponseEntity<?> subSidebar(@RequestParam(required = false) Long id){
            try{

                List<SubSidebarDto> data = sidebarServiceImpl.getListSub(id);
                return ResponseEntity.status(200).body(Map.of(
                    "message", "List found",
                    "status", true,
                    "statusCode", 200,
                    "data",data
              
                ));
            }catch(RuntimeException e){
                return ResponseEntity.status(422).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 422,
                    "errors", e.getStackTrace()
                ));
            }catch(Exception e){
                return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 400,
                    "errors", e.getStackTrace()
                ));
            }
    }



    @PatchMapping("/master-update/{id}")
    public ResponseEntity<?> mainSidebarUpdate(@PathVariable(required = false) Long id,@RequestBody Map<String, String> requestPayload){
            try{

                boolean data = sidebarServiceImpl.getListMasterUpdate(requestPayload.get("data"),id);
                return ResponseEntity.status(200).body(Map.of(
                    "message", "updated",
                    "status", true,
                    "statusCode", 200,
                    "data",data
              
                ));
            }catch(RuntimeException e){
                return ResponseEntity.status(422).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 422,
                    "errors", e.getStackTrace()
                ));
            }catch(Exception e){
                return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 400,
                    "errors", e.getStackTrace()
                ));
            }
    }

    @PatchMapping("/side-update/{id}")
    public ResponseEntity<?> sideSidebarUpdate(@PathVariable(required = false) Long id,@RequestBody Map<String, String> requestPayload){
            try{

                boolean data = sidebarServiceImpl.getListSideUpdate(requestPayload.get("data"),id);
                return ResponseEntity.status(200).body(Map.of(
                    "message", "updated",
                    "status", true,
                    "statusCode", 200,
                    "data",data
              
                ));
            }catch(RuntimeException e){
                return ResponseEntity.status(422).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 422,
                    "errors", e.getStackTrace()
                ));
            }catch(Exception e){
                return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 400,
                    "errors", e.getStackTrace()
                ));
            }
    }
}

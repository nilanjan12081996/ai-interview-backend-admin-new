package resume.miles.doctordays.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import resume.miles.doctordays.dto.DaysDTO;
import resume.miles.doctordays.service.DaysService;
import resume.miles.doctors.dto.DoctorDTO;
import resume.miles.doctors.service.DoctorService;

@RestController
@RequestMapping("/api/goodmood/days")
public class DaysController {
private DaysService daysService;
public DaysController(DaysService daysService){
    this.daysService=daysService;
}
@GetMapping("/get-all-days")
public ResponseEntity<Map<String,Object>> getAllDays(){
   try {
     List<DaysDTO> dtoList=daysService.getAllDays();
    return ResponseEntity.status(200).body(Map.of(
        "Message","Days found successfully",
        "status",true,
        "statusCode",200,
        "data",dtoList
     ));
   } catch (Exception e) {
   return ResponseEntity.status(400).body(Map.of(
            "Message",e.getMessage(),
            "status",false,
            "statusCode",400
    ));
   }
}

 @PatchMapping("/status/{id}")
    public ResponseEntity<?> toggleStatus(@PathVariable Long id)
    {
        try{
             DaysDTO dto=daysService.toggleStatus(id);
             return ResponseEntity.status(200).body(Map.of(
                "message","status change successfully",
                "status",true,
                "statusCode",200
             ));
        }catch(Exception e){
            return  ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "status",false,
                "statusCode",400
            ));
        }
       
    }
}
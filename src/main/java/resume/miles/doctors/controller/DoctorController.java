package resume.miles.doctors.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import resume.miles.doctors.dto.DoctorDTO;
import resume.miles.doctors.service.DoctorService;

@RestController
@RequestMapping("/api/goodmood/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    
    @GetMapping("/all-doctors")
    public ResponseEntity<Map<String,Object>> getAllDoctors(@RequestParam(required = false) Long id){
        try{
            if(id!=null){
                 DoctorDTO dto = doctorService.getDoctorById(id);

            return ResponseEntity.ok(Map.of(
                    "message", "Doctor fetched successfully",
                    "status", true,
                    "statusCode", 200,
                    "data", dto
            ));
            }else{
             List<DoctorDTO> dtoList=doctorService.getAllDoctors();
             return ResponseEntity.status(200).body(Map.of(
                "message","Data Fetch successfully",
                "status",true,
                "statusCode",200,
                "data",dtoList
             ));
            }
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "status",false,
                "statusCode",400
            ));
        }
    }
    @PatchMapping("/approve/{id}")
    public ResponseEntity<?> approveDoctor(@PathVariable Long id){
        try {
            DoctorDTO dto=doctorService.approveDoctor(id);
            return ResponseEntity.status(200).body(Map.of(
                "message","approve successfully",
                "status",true,
                "statusCode",200

            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "status",false,
                "statusCode",400
            ));
        }
    }
    @PatchMapping("/status/{id}")
    public ResponseEntity<?> toggleStatus(@PathVariable Long id)
    {
        try{
             DoctorDTO dto=doctorService.statusChange(id);
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

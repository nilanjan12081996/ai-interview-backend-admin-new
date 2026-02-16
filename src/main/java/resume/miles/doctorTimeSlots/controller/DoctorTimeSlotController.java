package resume.miles.doctorTimeSlots.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import resume.miles.doctorTimeSlots.dto.DoctorTimeSlotsDTO;
import resume.miles.doctorTimeSlots.service.DoctorTimeSlotService;

@RestController
@RequestMapping("/api/goodmood/doctor-time-slot")
public class DoctorTimeSlotController {
    @Autowired
    private  DoctorTimeSlotService  doctorTimeSlotService;
   
    @GetMapping("/slot-list")
    public ResponseEntity<Map<String, Object>> getAllSlot(@RequestParam(required = false) Long id){
        try {
            if(id!=null){
                 DoctorTimeSlotsDTO singleSlot=doctorTimeSlotService.getDoctorSlotById(id);
        return ResponseEntity.status(200).body(Map.of(
            "message","Data found successfully",
            "status",true,
            "statusCode",200,
            "data",singleSlot
        ));
            }
            else{
        List<DoctorTimeSlotsDTO> lists=doctorTimeSlotService.allTimeSlot();
        return ResponseEntity.status(200).body(Map.of(
            "message","Data found successfully",
            "status",true,
            "statusCode",200,
            "data",lists
        ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
            "message",e.getMessage(),
            "status",false,
            "statusCode",400
           
        ));
        }
    
    }
    @PutMapping("/update-slot/{id}")
    public ResponseEntity<?>updateSlot(@PathVariable Long id, @RequestBody DoctorTimeSlotsDTO dto){
        try{
            DoctorTimeSlotsDTO updateSlot=doctorTimeSlotService.updateSlot(id, dto);
         return ResponseEntity.status(200).body(
        Map.of(
                "message", "slot update successfully",
                "status", true,
                "statusCode", 200,
                "data", updateSlot
        )
);
        }catch(RuntimeException e){
            return ResponseEntity.status(422).body(Map.of(
                "message",e.getMessage(),
                "status",false,
                "statusCode",422
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "status",false,
                "statusCode",400
            ));
        }
        
        
    }

    @PatchMapping("/status/{id}")
        public ResponseEntity<?>toggleStatus(@PathVariable Long id){
            try {
                DoctorTimeSlotsDTO data=doctorTimeSlotService.toggleStatus(id);
                return ResponseEntity.status(200).body(Map.of(
                    "message","status change successfully",
                    "status",true,
                    "statusCode",200
                ));
            } catch (RuntimeException e) {
                return ResponseEntity.status(422).body(Map.of(
                    "message",e.getMessage(),
                    "status",false,
                    "statusCode",422
                ));
            }catch(Exception e){
                return ResponseEntity.status(400).body(Map.of(
                    "message",e.getMessage(),
                    "status",false,
                    "statusCode",400
                ));
            }
        }
    


}

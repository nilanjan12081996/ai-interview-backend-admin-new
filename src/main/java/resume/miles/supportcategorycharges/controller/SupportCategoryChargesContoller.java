package resume.miles.supportcategorycharges.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import resume.miles.supportcategorycharges.dto.SupportCategoryChargesDto;
import resume.miles.supportcategorycharges.service.SupportCategoryChargeService;

@RestController
@RequestMapping("/api/goodmood/charges")
@RequiredArgsConstructor
public class SupportCategoryChargesContoller {
    private final SupportCategoryChargeService service;
    @PostMapping("/add-update")
    public ResponseEntity<?> saveCharges(@RequestBody SupportCategoryChargesDto dto){
        try {
              SupportCategoryChargesDto data=service.saveCharges(dto);
        return ResponseEntity.status(200).body(Map.of(
           "message","data change successfully",
           "status",true,
           "statusCode",200,
           "data",data
        ));
            
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
           "message",e.getMessage(),
           "status",false,
           "statusCode",400
        ));
        }
      
    
    }
    @GetMapping("/get-charges/{categoryId}")
    public ResponseEntity<?> getCharges(@PathVariable Long categoryId){
        try {
            SupportCategoryChargesDto data=service.getCharges(categoryId);
            return ResponseEntity.status(200).body(Map.of(
                "message","Charges Found successfully",
                "status",true,
                "statusCode",200,
                "data",data
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                 "message",e.getMessage(),
                "status",false,
                "statusCode",400
              
            ));
        }
        

    }
}

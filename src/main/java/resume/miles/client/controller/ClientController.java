package resume.miles.client.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import resume.miles.client.dto.ClientDto;
import resume.miles.client.service.ClientService;

@RestController
@RequestMapping("/api/goodmood/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    @PostMapping("/create")
    public ResponseEntity<?> createClient(@RequestBody ClientDto clientDto) {
        try {
            ClientDto createdClient = clientService.createClient(clientDto);
            return ResponseEntity.status(201).body(Map.of(
                "message","Client created successfully",
                "status",true,
                "status_code",201
            ));
        } catch (Exception e) {
             return ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "status",false,
                "status_code",400
            ));
        }
        
        
    }
       @GetMapping("/create-list")
    public ResponseEntity<?> getAllClients() {
        try {
            List<ClientDto> clients = clientService.getAllClients();
            return ResponseEntity.status(200).body(Map.of(
                "message","client fetch successfully",
                "status",true,
                "status_code",200,
                "data",clients
            ));
        } catch (Exception e) {
              return ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "status", false,
                "status_code",400
            ));
        }

    }

       @GetMapping("/change-status")
    public ResponseEntity<?> getActiveClients() {
        try {
            List<ClientDto> clients = clientService.getActiveClients();
            return ResponseEntity.status(200).body(Map.of(
                "message","Status Change Successfully",
                "status",true,
                "status_code",200
            ));
        } catch (Exception e) {
             return ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "status",false,
                "status_code",400
            ));
        }
        
       
    }

}

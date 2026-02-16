package resume.miles.users.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import resume.miles.users.dto.UserDto;
import resume.miles.users.service.UserService;

@RestController
@RequestMapping("/api/goodmood/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
       @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try{
            return ResponseEntity.status(201).body(Map.of(
                "message","user created successfully",
                "data",userService.createUser(userDto),
                "status",true,
                "status_code",201
            ));
        }catch(Exception e){
            return ResponseEntity.status(400).body(Map.of(
                "message",e.getMessage(),
                "status",false,
                "status_code",400
            ));


        }
        
    }
     @GetMapping("/user-list")
    public ResponseEntity<?> getUsers() {
        try {
             List<UserDto> users = userService.getAllUsers();
            return ResponseEntity.status(200).body(Map.of(
                "message","User Fetch successfully",
                "data",users,
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

    @GetMapping("/single-user/{id}")
    public ResponseEntity<?> getSingleUser(@PathVariable Long id)
    {
        try {
            UserDto user=userService.getUserById(id);
            return ResponseEntity.status(200).body(Map.of(
                "message","user fetch successfully",
                "status",true,
                "status_code",200,
                "data",user
            ));

        } catch (Exception e) {
          return ResponseEntity.status(400).body(Map.of(
            "message",e.getMessage(),
            "status",false,
            "status_code",200
          ));
        }
    }

     @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
             userService.deleteUser(id);
        return ResponseEntity.status(200).body(Map.of(
            "message","User deleted successfully",
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

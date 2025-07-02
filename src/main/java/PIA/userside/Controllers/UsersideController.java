package PIA.userside.Controllers;


import PIA.userside.Models.Inputs.CandidateInput;
import PIA.userside.Models.Major;
import PIA.userside.Services.UsersideService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userside")
public class UsersideController {

    private final UsersideService usersideService;

    public UsersideController(UsersideService usersideService) {
        this.usersideService = usersideService;
    }

    @PostMapping("/application/add")
    public ResponseEntity<String> addApplication(@RequestBody CandidateInput candidateInput) {
        try {
            String result = usersideService.addApplication(candidateInput);
            return ResponseEntity.ok(result); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body("CONTROLLER::addApplication::Validation failed: " + e.getMessage()); // 400 Bad Request
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CONTROLLER::addApplication::Unexpected error: " + e.getMessage()); // 500 Internal Server Error
        }
    }

    @GetMapping("/get/majors")
    public ResponseEntity<?> getMajors() {
        try{
            List<String> result = usersideService.getMajors();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CONTROLLER::getMajors::ERROR::" + e.getMessage());

        }
    }

    @GetMapping("/get/universities")
    public ResponseEntity<?> getUniversities() {
        try{
            List<String> result = usersideService.getUniversities();
            return ResponseEntity.ok(result);
        }  catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CONTROLLER::getUniversities::ERROR::" + e.getMessage());
        }
    }


    // Test API
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        try {
            return ResponseEntity.ok("Hello World");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("CONTROLLER::test::Validation failed: " + e.getMessage());
        }
    }

}

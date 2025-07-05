// <editor-fold desc="IMPORTS">

package PIA.userside.Controllers;

import PIA.userside.Models.Inputs.CandidateDetailsInput;
import PIA.userside.Models.Inputs.CandidateInput;
import PIA.userside.Models.Inputs.Filter;
import PIA.userside.Models.Inputs.JobAndCityRequest;
import PIA.userside.Models.Major;
import PIA.userside.Models.Outputs.CandidateDetailsOutput;
import PIA.userside.Models.Outputs.FilterOutput;
import PIA.userside.Models.University;
import PIA.userside.Services.UsersideService;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// </editor-fold>

/*
TO DO:
1- WRITE FUNCTION CHAIN FOR TEST::DONE
2- Write getJobs API::DONE
3- Write getCandidateDetails API::DONE
4- Check GPA value in filter (must be between 0 and 4)::DONE

5- Write API for major addition::DONE
6- Write API for University addition::DONE
7- Do not allow duplicate values in jobName::DONE
8- Do not allow duplicate values in University::DONE
9- Do not allow duplicate values in Major::DONE
10- Do not allow duplicate values in City::DONE
11- Check, in application/add, jobName and CityName consistent::DONE

SUNDAY
12- PROPER LOG
13- Update API and Function List


API LIST (/userside)
- /application/add      (@RequestPart "candidate" -> CandidateInput candidate | "cv" -> MultipartFile cvFile)
- /get/majors           (NULL)
- /get/universities     (NULL)
- /download/cv          (@RequestParam: email)
- /filter/candidates    (@RequestBody: List<Filter> filters)

*/


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/userside")
public class UsersideController {

    private static final Logger logger = LoggerFactory.getLogger(UsersideService.class);
    private final UsersideService usersideService;

    // Constructor
    public UsersideController(UsersideService usersideService) {
        this.usersideService = usersideService;
    }


    // <editor-fold desc="Database Entity Save">
    @PostMapping(value = "/application/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addApplication(
            @RequestPart("candidate") CandidateInput candidateInput,
            @RequestPart("cv") MultipartFile cvFile) {
        try {
            System.out.println("CONTROLLER::applicationAdd::Executed");

            String result = usersideService.addApplication(candidateInput, cvFile);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("CONTROLLER::application/add::ERROR " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CONTROLLER::application/add::ERROR:: " + e.getMessage());
        }
    }

    @PostMapping("/jobandcity/add")
    public ResponseEntity<String> addJobAndCity(@RequestBody JobAndCityRequest request) {
        try {
            System.out.println("CONTROLLER::addJobAndCity::Executed");
            String result = usersideService.addJobandCity(request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("CONTROLLER::addJobAndCity::ValidationError: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CONTROLLER::addJobAndCity::UnexpectedError: " + e.getMessage());
        }
    }

    @PostMapping("/major/add")
    public ResponseEntity<String> addMajor(@RequestBody Major major){
        try {
            System.out.println("CONTROLLER::addMajor::Executed");
            String result = usersideService.addMajor(major);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("CONTROLLER::addMajor::ValidationError: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CONTROLLER::addMajor::UnexpectedError: " + e.getMessage());
        }
    }

    @PostMapping("/university/add")
    public ResponseEntity<String> addUniversity(@RequestBody University university){
        try {
            System.out.println("CONTROLLER::addUniversity::Executed");
            String result = usersideService.addUniversity(university);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("CONTROLLER::addUniversity::ValidationError: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CONTROLLER::addUniversity::UnexpectedError: " + e.getMessage());
        }
    }


    // </editor-fold>?

    // <editor-fold desc="Get Functions">
    @GetMapping("/get/majors")
    public ResponseEntity<?> getMajors() {
        try{
            logger.info("EXECUTING");
            List<String> result = usersideService.getMajors();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            logger.error("ERROR", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CONTROLLER::getMajors::ERROR:: " + e.getMessage());

        }
    }

    @GetMapping("/get/universities")
    public ResponseEntity<?> getUniversities() {
        try{
            logger.info("EXECUTING");
            List<String> result = usersideService.getUniversities();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            logger.error("ERROR", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CONTROLLER::getUniversities::ERROR:: " + e.getMessage());
        }
    }

    @GetMapping("/get/jobs")
    public ResponseEntity<?> getJobs() {
        try{
            logger.info("EXECUTING");
            List<String> result = usersideService.getJobs();
            return ResponseEntity.ok(result);
        }  catch (RuntimeException e) {
            logger.error("ERROR", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CONTROLLER::getJobs::ERROR:: " + e.getMessage());
        }
    }

    @GetMapping("/get/cities")
    public ResponseEntity<?> getCities() {
        try{
            logger.info("EXECUTING");

            List<String> result = usersideService.getCities();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            logger.error("ERROR", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CONTROLLER::getCities::ERROR:: " + e.getMessage());
        }
    }
    // </editor-fold>



    @PostMapping("/get/candidate/details")
    public ResponseEntity<?> getCandidateDetails(@RequestBody CandidateDetailsInput candidateDetailsInput) {
        try {
            logger.info("EXECUTING");

            CandidateDetailsOutput candidateDetailsOutput = usersideService.getCandidateDetails(candidateDetailsInput);
            return ResponseEntity.ok(candidateDetailsOutput);
        }  catch (RuntimeException e) {
            logger.error("ERROR", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CONTROLLER::getCandidateDetails::ERROR:: " + e.getMessage());
        }
    }

    @PostMapping("/download/cv")
    public ResponseEntity<?> downloadCV(@RequestParam String email) {
        try {
            logger.info("EXECUTING");

            byte[] cvFile = usersideService.downloadCV(email);

            if (cvFile == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("CONTROLLER::downloadCV::ERROR:: No CV found for the given email");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "attachment; filename=\"cv.pdf\"")
                    .body(cvFile);
        } catch (RuntimeException e) {
            logger.error("ERROR", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("CONTROLLER::downloadCV::ERROR:: " + e.getMessage()).getBytes());
        }
    }

    @PostMapping("/filter/candidates")
    public ResponseEntity<?> filterCandidates(@RequestBody List<Filter> filters){
        try{
            logger.info("EXECUTING");

            List<FilterOutput> result = usersideService.filterCandidates(filters);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            logger.error("ERROR", e);
            return ResponseEntity.badRequest().body("CONTROLLER::filterCandidates::ERROR:" + e.getMessage());
        } catch (RuntimeException e) {
            logger.error("ERROR", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CONTROLLER::filterCandidates::ERROR:: " + e.getMessage());
        }
    }


    // <editor-fold desc="Test API">
    @PostMapping(value = "/uploadtest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadTest(
            @RequestPart("candidate") CandidateInput candidateInput,
            @RequestPart("cv") MultipartFile cvFile) {
        try {
            return ResponseEntity.ok("Hello World");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("CONTROLLER::test::Validation failed: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        try {
            return ResponseEntity.ok("Hello World");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("CONTROLLER::test::Validation failed: " + e.getMessage());
        }
    }
    // </editor-fold>

}

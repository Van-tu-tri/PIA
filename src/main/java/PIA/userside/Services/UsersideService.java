// <editor-fold desc="IMPORTS">
package PIA.userside.Services;


import PIA.userside.Models.*;
import PIA.userside.Models.Inputs.CandidateDetailsInput;
import PIA.userside.Models.Inputs.CandidateInput;
import PIA.userside.Models.Inputs.Filter;
import PIA.userside.Models.Inputs.JobAndCityRequest;
import PIA.userside.Models.Outputs.CandidateDetailsOutput;
import PIA.userside.Models.Outputs.FilterOutput;
import PIA.userside.Repositories.UsersideRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

import static PIA.userside.GlobalData.Constants.*;

// </editor-fold>

/*
FUNCTIONS

-HELPER FUNCTIONS
-- capitalizeName           (String name)
-- normalizeSex             (String sex)
-- normalizeEnglishLevel    (String level)
-- normalizeCurrentYear     (String year)

@Transactional
-- addApplication   (CandidateInput candidate, MultiparFile cvFile)
    -> capitalizeName, normalizeSex, normalizeEnglishLevel, normalizeCurrentYear
    -> usersideRepository.getMajor(String major) , usersideRepository.getJob(String job), usersideRepository.getCity(String city)
    -> usersideRepository.getUniversity(String university), usersideRepository.getCandidateId(String email)
    -> addCandidate(Candidate  c)
    -> addCandidateCV(CandidateCV cv)
    -> addCandidateJob(CandidateJob)

SAVING
return type: String (Success Message)

-- addCandidate(Candidate c)
    -> usersideRepository.addCandidate(Candidate c)
-- addCity(City city)
    -> usersideRepository.addCity(City city)
-- addJob(Job job)
    -> usersideRepository.addJob(Job job)
-- addMajor(Major major)
    -> usersideRepository.addMajor(Major major)
-- addUniversity(University university)
    -> usersideRepository.addUniversity(University university)
-- addJobCity(Jobcity jobcity)
    -> usersideRepository.addJobCity(JobCity jobcity)
-- addCandidateJob(CandidateJob candidateJob)
    -> usersideRepository.addCandidateJob(CandidateJob candidateJob)
-- addCandidateCV(CandidateCV cv)
    -> usersideRepository.addCandidateCV(CandidateCV cv)

GET

-- getMajors()
    -> usersideRepository.getMajors()
    Return Type:    List<String>
-- getUniversities()
    -> usersideRepository.getUniversities()
    Return Type:    List<String>
-- getJobs()
    -> usersideRepository.getJobs()
    Return Type:    List<String>
-- downloadCV(String email)
    -> usersideRepository.getCandidateCV(String email)
    Return Type:    byte[]

FILTER

-- filterCandidates(List<Filter> filters)
    -> usersideRepository.filterCandidate(List<Filter> filters)
    Return Type:    List<FilterOutput>

 */

@Service
public class UsersideService {

    private static final Logger logger = LoggerFactory.getLogger(UsersideService.class);

    private final UsersideRepository usersideRepository;
    public UsersideService(UsersideRepository usersideRepository) {
        this.usersideRepository = usersideRepository;
    }


    // <editor-fold desc="Helper Functions">
    public String capitalizeName(String name) {
        logger.info("EXECUTING: capitalizeName.");
        if (name == null || name.isBlank()) return name;

        String[] words = name.trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return result.toString().trim();
    }

    public String normalizeSex(String sex) {
        logger.info("EXECUTING: normalizeSex.");
        if (sex == null) return null;

        String normalized = sex.trim().toLowerCase();
        if (sexes.contains(normalized)) {
            return normalized;
        } else {
            return null;
        }
    }

    public String normalizeEnglishLevel(String level) {
        logger.info("EXECUTING: normalizeEnglishLevel.");
        if (level == null) return null;

        String normalized = level.trim().toLowerCase();
        if (englishLevels.contains(normalized)) {
            return normalized;
        } else {
            return null;
        }
    }

    public String normalizeCurrentYear(String year) {
        logger.info("EXECUTING: normalizeCurrentYear.");
        if (year == null) return null;

        String normalized = year.trim().toLowerCase();
        if (years.contains(normalized)) {
            return normalized;
        } else {
            return null;
        }
    }
    //</editor-fold>


    // <editor-fold desc="ADD APPLICATION">

    @Transactional
    // Add Multipart Later::DONE
    public String addApplication(CandidateInput candidate, MultipartFile cvFile) {
        try{
            logger.info("EXECUTING: addApplication.");

            List<String> nullErrors = new ArrayList<>();
            List<String> logicErrors = new ArrayList<>();

            // <editor-fold desc="Null Checks">
            // CHECKS
            String name = candidate.getCandidateName();
            if (name == null || name.isBlank()) {
                nullErrors.add("candidateName");
            }
            else{
                name = capitalizeName(name);
                candidate.setCandidateName(name);
            }

            String surname = candidate.getCandidateSurname();
            if (surname == null || surname.isBlank()) {
                nullErrors.add("candidateSurname");
            }
            else {
                surname = capitalizeName(surname);
                candidate.setCandidateSurname(surname);
            }

            LocalDate birthDay = candidate.getCandidateBirthDay();
            if (birthDay == null) {
                nullErrors.add("candidateBirthDay");
            } else if (birthDay.isAfter(LocalDate.now())) {
                logicErrors.add("candidateBirthDay can not be in the future.");
            }

            String email = candidate.getCandidateEmail();
            if (email == null || email.isBlank()) {
                nullErrors.add("candidateEmail");
            }

            String phone = candidate.getCandidatePhone();
            if (phone == null || phone.isBlank()) {
                nullErrors.add("candidatePhone");
            }

            String university = candidate.getCandidateUniversity();
            if (university == null || university.isBlank()) {
                nullErrors.add("candidateUniversity");
            }

            String major = candidate.getCandidateMajor();
            if (major == null || major.isBlank()) {
                nullErrors.add("candidateMajor");
            }

            BigDecimal gpa = candidate.getCandidateGPA();
            if (gpa == null) {
                nullErrors.add("candidateGPA");
            } else if (gpa.compareTo(BigDecimal.ZERO) < 0 || gpa.compareTo(new BigDecimal("4.0")) > 0) {
                logicErrors.add("candidateGPA must be between 0 and 4");
            }

            String sex = candidate.getCandidateSex();
            if (sex == null || sex.isBlank()) {
                nullErrors.add("candidateSex");
            }
            else {
                sex = normalizeSex(sex);
                if (sex == null) {
                    logicErrors.add("candidateSex must be either 'male' or 'female'");
                }
            }

            String currentYear = candidate.getCandidateCurrentYear();
            if (currentYear == null || currentYear.isBlank()) {
                nullErrors.add("candidateCurrentYear");
            }
            else {
                currentYear = normalizeCurrentYear(currentYear);
                if (currentYear == null) {
                    logicErrors.add("candidateCurrentYear must be in [freshman, sophomore, junior, senior, graduate]");
                }
            }
            
            Integer expectedGraduateYear = candidate.getCandidateExpectedGraduateYear();
            if (expectedGraduateYear == null) {
                nullErrors.add("expectedGraduateYear");
            }

            String englishLevel = candidate.getCandidateEnglishLevel();
            if (englishLevel == null || englishLevel.isBlank()) {
                nullErrors.add("candidateEnglishLevel");
            }
            else {
                englishLevel = normalizeEnglishLevel(englishLevel);
                if (englishLevel == null) {
                    logicErrors.add("candidateEnglishLevel must be in [a1, a2, b1, b2, c1, c2]");
                }
            }

            // Additional fields (job, city, applicationDate)
            String job = candidate.getJobName();
            if (job == null || job.isBlank()) {
                nullErrors.add("jobName");
            }

            String city = candidate.getCityName();
            if (city == null || city.isBlank()) {
                nullErrors.add("cityName");
            }

            if (cvFile == null || cvFile.isEmpty()) {
                nullErrors.add("cvFile");
            }
            // </editor-fold>

            // <editor-fold desc="Error Message Set">
            // Null Error Set
            if (!nullErrors.isEmpty()) {
                String errorMessage = String.join(", ", nullErrors) + " can not be null";
                logger.error("ERROR: " + errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }

            // Logic Error Set
            if (!logicErrors.isEmpty()) {
                String errorMessage = String.join("\n", logicErrors);
                logger.error("ERROR: " + errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
            // </editor-fold>

            // <editor-fold desc="Foreign Key Setting For University, Major, Job and City. Also get candidateId">
            List<String> notPresent = new ArrayList<>();
            Integer majorId = usersideRepository.getMajor(major);
            // Job id and City Id must be consistent : CHECK IT WITH JOBSCITIES TABLE
            Integer jobId = usersideRepository.getJob(job);
            Integer cityId = usersideRepository.getCity(city);
            if (majorId == null){
                notPresent.add("Major: "  + major);
            }
            if (jobId == null){
                notPresent.add("Job: "  + job);
            }
            if (cityId == null){
                notPresent.add("City: "  + city);
            }
            if (!notPresent.isEmpty()) {
                String errorMessage = String.join(", ", notPresent) + " is not present in the database";
                logger.error("ERROR: " + errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
            // Check Job-City exists
            Integer jobCityId = usersideRepository.getJobCity(jobId, cityId);
            if (jobCityId == null){
                logger.error("ERROR: This job is not associated with city: " + city);
                throw new IllegalArgumentException("This job is not associated with city: " + city);
            }

            // Add university if university is not present in the database
            Integer universityId = usersideRepository.getUniversity(university);
            if (universityId == null){
                University u = new  University();
                u.setUniversityName(university);
                addUniversity(u);
            }
            universityId = usersideRepository.getUniversity(university);
            // </editor-fold>

            // <editor-fold desc="Create Candidate">
            Candidate c = new Candidate();
            c.setCandidateName(name);
            c.setCandidateSurname(surname);
            c.setCandidateBirthDay(birthDay);
            c.setCandidateEmail(email);
            c.setCandidatePhone(phone);
            c.setCandidateUniversity(universityId);
            c.setCandidateMajor(majorId);
            c.setCandidateGPA(gpa);
            c.setCandidateSex(sex);
            c.setCandidateCurrentYear(currentYear);
            c.setCandidateExpectedGraduateYear(expectedGraduateYear);
            c.setCandidateEnglishLevel(englishLevel);
            addCandidate(c);
            // </editor-fold>

            // <editor-fold desc="Create CandidateJob">
            Integer candidateId = usersideRepository.getCandidateId(email);
            if (candidateId == null){
                logger.error("ERROR: Can not found Candidate with email: " + email);
                throw new IllegalArgumentException("SERVICE::addApplication::Can not found Candidate with email: " + email);
            }

            CandidateJob cj = new CandidateJob();
            cj.setApplicationDate(LocalDate.now());
            cj.setStatus("pending");
            cj.setCandidateId(candidateId);
            cj.setCityId(cityId);
            cj.setJobId(jobId);
            addCandidateJob(cj);
            // </editor-fold>

            // <editor-fold desc="Save CV">

            CandidateCV cv = new CandidateCV();
            cv.setCandidateId(candidateId);
            cv.setCvFile(cvFile.getBytes());
            cv.setCvFileName(cvFile.getOriginalFilename());
            addCandidateCV(cv);
            // </editor-fold>

            return "SERVICE::addApplication::Success";
        } catch (IOException e) {
            logger.error("ERROR: Failed to read CV file", e);
            throw new RuntimeException("SERVICE::addApplication::ERROR::Failed to read CV file", e);
        } catch (RuntimeException e) {
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::addApplication::Error:: " + e.getMessage(), e);
        }
    }


    // </editor-fold>

    // IMPORTANT: THESE FUNCTIONS DO NOT CHECK INPUT, BE CAREFUL WHILE USING IT
    // <editor-fold desc="SERVICE DB SAVE">

    // Duplicate Check: Database Level, the only important part is Email and Phone, and they are UNIQUE
    public String addCandidate(Candidate candidate) {
        try{
            logger.info("EXECUTING");
            usersideRepository.addCandidate(candidate);
            return "SERVICE::addCandidate::Success";
        } catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::addCandidate::Error::" + e.getMessage(), e);
        }
    }

    // Duplicate Check: Done
    @Transactional
    public String addJobandCity(JobAndCityRequest request) {
        try{
            logger.info("EXECUTING");
            Job job = new  Job();
            job.setJobName(request.getJobName());
            job.setJobStatus(request.getJobStatus().name());
            addJob(job);

            City city = new  City();
            city.setCityName(request.getCityName());
            addCity(city);

            // Will Throw Error if already added.

            JobCity jobCity = new JobCity();
            int jobId = usersideRepository.getJob(job.getJobName());
            int cityId = usersideRepository.getCity(city.getCityName());
            jobCity.setJobId(jobId);
            jobCity.setCityId(cityId);
            addJobCity(jobCity);
            // Check does not necessary, jobId-cityId is already Primary Key
            return "SERVICE::addJobandCity::Success";
        }  catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::addJobandCity::Error::" + e.getMessage(), e);
        }
    }

    // Duplicate Check: Done
    public String addCity(City city) {
        try{
            logger.info("EXECUTING");
            Integer cityId = usersideRepository.getCity(city.getCityName());
            if (cityId != null){
                logger.warn("WARN: City already exists");
                return "SERVICE::addCity::Pass:: City already exists";
            }
            usersideRepository.addCity(city);
            return "SERVICE::addCity::Success";
        }  catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::addCity::Error::" + e.getMessage(), e);
        }
    }

    // Duplicate Check: Done
    public String addJob(Job job) {
        try{
            logger.info("EXECUTING");
            Integer jobId = usersideRepository.getJob(job.getJobName());
            if (jobId != null){
                logger.warn("WARN: Job already exists");
                return "SERVICE::addJob::Pass:: Job already exists";
            }
            usersideRepository.addJob(job);
            return "SERVICE::addJob::Success";
        } catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::addJob::Error::" + e.getMessage(), e);
        }
    }

    // Duplicate Check: Done
    public String addMajor(Major major) {
        try{
            logger.info("EXECUTING");
            Integer majorId = usersideRepository.getMajor(major.getMajorName());
            if (majorId != null){
                logger.warn("WARN: Major already exists");
                throw new DuplicateKeyException("SERVICE::addMajor::ERROR:: This major has already been added");
            }
            usersideRepository.addMajor(major);
            return "SERVICE::addMajor::Success";
        } catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::addMajor::ERROR:: " + e.getMessage(), e);
        }
    }

    // Duplicate Check: Done
    public String addUniversity(University university) {
        try{
            logger.info("EXECUTING");
            Integer universityId = usersideRepository.getUniversity(university.getUniversityName());
            if (universityId != null){
                logger.warn("WARN: University already exists");
                throw new DuplicateKeyException("SERVICE::addMajor::ERROR:: This university has already been added");
            }
            usersideRepository.addUniversity(university);
            return "SERVICE::addUniversity::Success";
        } catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::addUniversity::Error::" + e.getMessage(), e);
        }
    }


    // Relations

    // Duplicate Check: Does not necessary (JobId-CityId is primary Key)
    public String addJobCity(JobCity jobCity) {
        try{
            logger.info("EXECUTING");
            usersideRepository.addJobCity(jobCity);
            return "SERVICE::addJobCity::Success";
        }  catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::addJobCity::Error::" + e.getMessage(), e);
        }
    }

    // Consistencty Check: Done
    public String addCandidateJob(CandidateJob candidateJob) {
        try{
            logger.info("EXECUTING");
            // City Id must be consistent with jobCity Table.
            Integer jobId = candidateJob.getJobId();
            Integer cityId = candidateJob.getCityId();
            Integer found = usersideRepository.getJobCity(cityId, jobId);
            if (found == null){
                logger.error("ERROR: This job does not related with this city");
                throw new IllegalArgumentException("SERVICE::addCandidateJob::ERROR:: This job does not related with this city");
            }
            usersideRepository.addCandidateJob(candidateJob);
            return "SERVICE::addCandidateJob::Success";
        }  catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::addCandidateJob::Error::" + e.getMessage(), e);
        }
    }

    public String addCandidateCV(CandidateCV candidateCV) {
        try {
            logger.info("EXECUTING");
            usersideRepository.addCandidateCV(candidateCV);
            return "SERVICE::addCandidateCV::Success";
        } catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::addCandidateCV::Error::" + e.getMessage(), e);
        }
    }
    // </editor-fold>


    // <editor-fold desc="SERVICE DB GET">
    public List<String> getMajors(){
        logger.info("EXECUTING");
        try{
            return usersideRepository.getMajors();
        } catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::getMajors::Error::" + e.getMessage(), e);
        }
    }

    public List<String> getUniversities(){
        logger.info("EXECUTING");
        try{
            return usersideRepository.getUniversities();
        } catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::getUniversities::Error::" + e.getMessage(), e);
        }
    }

    public List<String> getJobs(){
        try{
            logger.info("EXECUTING");
            return usersideRepository.getJobs();
        }  catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::getJobs::Error::" + e.getMessage(), e);
        }
    }

    public List<String> getCities(){
        try{
            logger.info("EXECUTING");
            return usersideRepository.getCities();
        } catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::getCities::Error::" + e.getMessage(), e);
        }
    }

    public CandidateDetailsOutput getCandidateDetails(CandidateDetailsInput candidateDetailsInput) {
        try{
            logger.info("EXECUTING");
            String candidateEmail = candidateDetailsInput.getCandidateEmail();
            String jobName = candidateDetailsInput.getJobName();
            if (candidateEmail == null || candidateEmail.isEmpty() || candidateEmail.isBlank()){
                logger.warn("WARN: CandidateEmail is empty");
                throw new RuntimeException("SERVICE::getCandidateDetails::ERROR:: candidateEmail is null or empty");
            }
            if (jobName == null || jobName.isEmpty() || jobName.isBlank()){
                logger.warn("WARN: jobName is empty");
                throw new RuntimeException("SERVICE::getCandidateDetails::ERROR:: jobName is null or empty");
            }
            return usersideRepository.getCandidateDetails(candidateEmail, jobName);
        }   catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::getJobs::Error::" + e.getMessage(), e);
        }
    }

    public byte[] downloadCV(String email){
        try {
            logger.info("EXECUTING");
            return usersideRepository.getCandidateCV(email);
        } catch (RuntimeException e){
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::downloadCV::Error::" + e.getMessage(), e);
        }
    }
    // </editor-fold>


    // CHECK LATER, MOST PARTS WRITTEN BY CHAT GPT: DONE
    // <editor-fold desc="FILTER FUNCTION">


    public List<FilterOutput> filterCandidates(List<Filter> filters) {
        logger.info("EXECUTING");
        try {
            for (Filter filter : filters) {
                // These functions both check the list itself, and its elements
                // CHECK: CAN NOT BE EMPTY

                // <editor-fold desc="List Check">
                if (filter.getCandidateUniversities() != null) {
                    if (filter.getCandidateUniversities().isEmpty() ||
                            filter.getCandidateUniversities().stream().anyMatch(s -> s == null || s.trim().isEmpty())) {
                        throw new IllegalArgumentException("candidateUniversities cannot be empty or contain blank values.");
                    }
                }

                if (filter.getCandidateMajors() != null) {
                    if (filter.getCandidateMajors().isEmpty() ||
                            filter.getCandidateMajors().stream().anyMatch(s -> s == null || s.trim().isEmpty())) {
                        throw new IllegalArgumentException("candidateMajors cannot be empty or contain blank values.");
                    }
                }

                if (filter.getJobNames() != null) {
                    if (filter.getJobNames().isEmpty() ||
                            filter.getJobNames().stream().anyMatch(s -> s == null || s.trim().isEmpty())) {
                        throw new IllegalArgumentException("jobNames cannot be empty or contain blank values.");
                    }
                }

                if (filter.getCityNames() != null) {
                    if (filter.getCityNames().isEmpty() ||
                            filter.getCityNames().stream().anyMatch(s -> s == null || s.trim().isEmpty())) {
                        throw new IllegalArgumentException("cityNames cannot be empty or contain blank values.");
                    }
                }

                // </editor-fold>

                // Enum types can not have blank or whitespace values, that is why we do not need to check te elements
                // <editor-fold desc="Enum Checks"
                if (filter.getCandidateSexes() != null && filter.getCandidateSexes().isEmpty()) {
                    throw new IllegalArgumentException("candidateSexes cannot be empty if provided.");
                }

                if (filter.getCandidateCurrentYears() != null && filter.getCandidateCurrentYears().isEmpty()) {
                    throw new IllegalArgumentException("candidateCurrentYears cannot be empty if provided.");
                }

                if (filter.getCandidateEnglishLevels() != null && filter.getCandidateEnglishLevels().isEmpty()) {
                    throw new IllegalArgumentException("candidateEnglishLevels cannot be empty if provided.");
                }

                if (filter.getCandidateApplicationStatuses() != null && filter.getCandidateApplicationStatuses().isEmpty()) {
                    throw new IllegalArgumentException("candidateApplicationStatuses cannot be empty if provided.");
                }
                // </editor-fold>


                // <editor-fold desc="Numeric Checks">
                if (filter.getCandidateExpectedGraduateYears() != null && filter.getCandidateExpectedGraduateYears().isEmpty()) {
                    throw new IllegalArgumentException("candidateExpectedGraduateYears cannot be empty if provided.");
                }

                if (filter.getApplicationDates() != null && filter.getApplicationDates().isEmpty()) {
                    throw new IllegalArgumentException("applicationDates cannot be empty if provided.");
                }

                if (filter.getCandidateMinGPA() != null && filter.getCandidateMaxGPA() != null) {
                    if (filter.getCandidateMinGPA().compareTo(filter.getCandidateMaxGPA()) > 0) {
                        throw new IllegalArgumentException("candidateMinGPA cannot be greater than candidateMaxGPA.");
                    }
                    if (filter.getCandidateMinGPA().compareTo(BigDecimal.ZERO) < 0) {
                        throw new IllegalArgumentException("candidateMinGPA cannot be less than 0.");
                    }
                    if (filter.getCandidateMinGPA().compareTo(new BigDecimal("4.0")) > 0) {
                        throw new IllegalArgumentException("candidateMinGPA cannot be greater than 4.");
                    }
                    if (filter.getCandidateMaxGPA().compareTo(BigDecimal.ZERO) < 0) {
                        throw new IllegalArgumentException("CandidateMaxGPA cannot be less than 0.");
                    }
                    if (filter.getCandidateMaxGPA().compareTo(new BigDecimal("4.0")) > 0) {
                        throw new IllegalArgumentException("CandidateMaxGPA cannot be greater than 4.");
                    }
                }

                if (filter.getCandidateMinAge() != null && filter.getCandidateMaxAge() != null) {
                    if (filter.getCandidateMinAge() > filter.getCandidateMaxAge()) {
                        throw new IllegalArgumentException("candidateMinAge cannot be greater than candidateMaxAge.");
                    }
                }

                if (filter.getApplicationMinDate() != null && filter.getApplicationMaxDate() != null) {
                    if (filter.getApplicationMinDate().isAfter(filter.getApplicationMaxDate())) {
                        throw new IllegalArgumentException("applicationMinDate cannot be after applicationMaxDate.");
                    }
                }
                // </editor-fold>
            }

            return usersideRepository.filterCandidates(filters);

        } catch (IllegalArgumentException e) {
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::filterCandidates::Invalid input: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            logger.error("ERROR: " + e.getMessage(), e);
            throw new RuntimeException("SERVICE::filterCandidates::Error: " + e.getMessage(), e);
        }
    }


    // </editor-fold>
}





















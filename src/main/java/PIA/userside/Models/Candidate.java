package PIA.userside.Models;

import PIA.userside.Models.Inputs.CandidateInput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Candidate {
    private int candidateId;
    private String candidateName;
    private String candidateSurname;
    private LocalDate candidateBirthDay;

    // Unique
    private String candidateEmail;
    private String candidatePhone;

    // Foreign Key
    private Integer candidateUniversity;
    private Integer candidateMajor;

    private BigDecimal candidateGPA;

    // Built in enums;
    private String candidateSex;
    private String candidateCurrentYear;
    private Integer candidateExpectedGraduateYear;
    private String candidateEnglishLevel;




}

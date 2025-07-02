package PIA.userside.Models.Inputs;

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
public class CandidateInput {
    private String candidateName;
    private String candidateSurname;
    private LocalDate candidateBirthDay;

    // Unique
    private String candidateEmail;
    private String candidatePhone;

    // Foreign Key
    private String candidateUniversity;
    private String candidateMajor;

    private BigDecimal candidateGPA;

    // Built in enums;
    private String candidateSex;
    private String candidateCurrentYear;
    private Integer candidateExpectedGraduateYear;
    private String candidateEnglishLevel;

    private String jobName;
    private String cityName;
    private LocalDate applicationDate;

}

package PIA.userside.Models.Outputs;

import PIA.userside.GlobalData.Application_Status;
import PIA.userside.GlobalData.Job_Status;
import PIA.userside.Models.Candidate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CandidateDetailsOutput {
    @Override
    public String toString() {
        return "CandidateDetailsOutput{" +
                "candidateName='" + candidateName + '\'' +
                ", candidateSurname='" + candidateSurname + '\'' +
                ", candidateBirthDay=" + candidateBirthDay +
                ", candidateEmail='" + candidateEmail + '\'' +
                ", candidatePhone='" + candidatePhone + '\'' +
                ", candidateUniversity='" + candidateUniversity + '\'' +
                ", candidateMajor='" + candidateMajor + '\'' +
                ", candidateGPA=" + candidateGPA +
                ", candidateSex='" + candidateSex + '\'' +
                ", candidateCurrentYear='" + candidateCurrentYear + '\'' +
                ", candidateExpectedGraduateYear=" + candidateExpectedGraduateYear +
                ", candidateEnglishLevel='" + candidateEnglishLevel + '\'' +
                ", jobName='" + jobName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", applicationStatus=" + applicationStatus +
                '}';
    }

    private String candidateName;
    private String candidateSurname;
    private LocalDate candidateBirthDay;

    private String candidateEmail;
    private String candidatePhone;

    private String candidateUniversity;
    private String candidateMajor;
    private BigDecimal candidateGPA;

    private String candidateSex;
    private String candidateCurrentYear;
    private Integer candidateExpectedGraduateYear;
    private String candidateEnglishLevel;

    private String jobName;
    private String cityName;
    private Application_Status applicationStatus;
}

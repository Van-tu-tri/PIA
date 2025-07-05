package PIA.userside.Models.Outputs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterOutput {
    private String candidateEmail;
    private String candidateName;
    private String candidateSurname;
    private String jobName;
    private String candidateUniversity;
    private String candidateMajor;
    private String status;
    private Integer candidateAge;
    private BigDecimal candidateGPA;
    private String candidateCurrentYear;


}

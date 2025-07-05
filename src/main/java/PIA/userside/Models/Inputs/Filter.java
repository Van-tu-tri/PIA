package PIA.userside.Models.Inputs;

import PIA.userside.GlobalData.Application_Status;
import PIA.userside.GlobalData.Current_Year;
import PIA.userside.GlobalData.English_Level;
import PIA.userside.GlobalData.Sex;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Filter {

    private Integer candidateMinAge;
    private Integer candidateMaxAge;

    private List<String> candidateUniversities;
    private List<String> candidateMajors;

    private BigDecimal candidateMinGPA;
    private BigDecimal candidateMaxGPA;

    private List<Sex> candidateSexes;
    private List<Current_Year> candidateCurrentYears;
    private List<Integer> candidateExpectedGraduateYears;
    private List<English_Level> candidateEnglishLevels;
    private List<Application_Status>  candidateApplicationStatuses;

    private List<String>  jobNames;
    private List<String> cityNames;

    private List<LocalDate> applicationDates;
    private LocalDate applicationMinDate;
    private LocalDate applicationMaxDate;

}

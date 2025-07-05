package PIA.userside.Models.Inputs;


import PIA.userside.GlobalData.Job_Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobAndCityRequest {
    private String jobName;
    private Job_Status jobStatus;
    private String cityName;
}

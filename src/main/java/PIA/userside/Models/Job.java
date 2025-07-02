package PIA.userside.Models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Job {
    private int jobId;
    private String jobName;

    // Built in Enum
    private String jobStatus;
}

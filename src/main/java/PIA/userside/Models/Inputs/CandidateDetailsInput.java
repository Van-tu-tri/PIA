package PIA.userside.Models.Inputs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CandidateDetailsInput {
    private String candidateEmail;
    private String jobName;
}

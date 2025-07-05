package PIA.userside.Models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CandidateCV {
    private Integer cvId;
    private Integer candidateId;
    private byte[] cvFile;
    private String cvFileName;
}

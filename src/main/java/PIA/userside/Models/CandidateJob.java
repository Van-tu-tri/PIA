package PIA.userside.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CandidateJob {
    private int candidateId;
    private int JobId;
    private int cityId;
    private LocalDate applicationDate;
    private String status;
}

package PIA.userside.Models;

import lombok.*;

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

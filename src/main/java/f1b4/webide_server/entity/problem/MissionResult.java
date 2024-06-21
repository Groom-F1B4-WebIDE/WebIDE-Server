package f1b4.webide_server.entity.problem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class MissionResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long problemId;

    private String memberEmail; //  -> id

    @Column(length = 10000)
    private String code;

    private boolean isCorrect;
    @Column(name = "result", length = 1000)
    private String result;

    private LocalDateTime resultTimeAt;
}

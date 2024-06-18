package f1b4.webide_server.domain.problem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Problem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private long timeLimit; // 5000 -> 5초
    private long memoryLimit; // 256 * 1024 * 1024 -> 256 MB

    @OneToMany(mappedBy = "problem" , cascade = CascadeType.ALL)
    private List<TestCase> testCases;
}

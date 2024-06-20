package f1b4.webide_server.entity.problem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class TestCase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String input;
    private String expectedOutput;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

//    public TestCase(String input, String expectedOutput) {
//        this.input = input;
//        this.expectedOutput = expectedOutput;
//    }
}

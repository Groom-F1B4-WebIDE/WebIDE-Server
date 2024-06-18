package f1b4.webide_server.domain.problem;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Problem {
    private Long id;
    private String title;
    private String description;
    private long timeLimit;

    private long memoryLimit; // 256 * 1024 * 1024 -> 256 MB

    @Setter @Getter
    private List<TestCase> testCases;

}
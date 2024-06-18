package f1b4.webide_server.domain.problem;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TestCase {
    private String input;
    private String expectedOutput;

    public TestCase(String input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }
}

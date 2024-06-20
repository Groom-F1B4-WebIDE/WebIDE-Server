package f1b4.webide_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TestCaseDTO {
    private Long id;
    private String input;
    private String expectedOutput;
}

package f1b4.webide_server.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ProblemDTO {
//    private Long id;
    private String title;
    private String description;
    private Long timeLimit;
    private Long memoryLimit;
    private List<TestCaseDTO> testCases;
}

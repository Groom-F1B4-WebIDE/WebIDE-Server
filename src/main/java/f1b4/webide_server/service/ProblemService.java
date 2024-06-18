package f1b4.webide_server.service;


import f1b4.webide_server.domain.problem.Problem;
import f1b4.webide_server.domain.problem.TestCase;
import f1b4.webide_server.dto.ProblemDTO;
import f1b4.webide_server.dto.TestCaseDTO;
import f1b4.webide_server.repository.ProblemRepository;
import f1b4.webide_server.repository.TestCaseRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Service
@RequiredArgsConstructor
public class ProblemService {
    private final List<Problem> problems;
    private final ProblemRepository problemRepository;

    private final TestCaseRepository testCaseRepository;

    public Problem getProblemById(Long id) {
        return problems.stream()
                .filter(problem -> problem.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Problem not found"));
    }

    @Transactional
    public Problem createProblem(ProblemDTO problemDTO) {
        Problem problem = new Problem();
        problem.setTitle(problemDTO.getTitle());
        problem.setDescription(problemDTO.getDescription());
        problem.setTimeLimit(problemDTO.getTimeLimit());
        problem.setMemoryLimit(problemDTO.getMemoryLimit());

        problem = problemRepository.save(problem);

        List<TestCase> testCases = new ArrayList<>();
        for (TestCaseDTO testCaseDTO : problemDTO.getTestCases()) {
            TestCase testCase = new TestCase();
            testCase.setInput(testCaseDTO.getInput());
            testCase.setExpectedOutput(testCaseDTO.getExpectedOutput());
            testCase.setProblem(problem);
            testCases.add(testCase);
        }

        testCaseRepository.saveAll(testCases);

        return problem;
    }

    @Transactional(readOnly = true)
    public ProblemDTO getProblem(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(() -> new RuntimeException("Problem not found"));

        ProblemDTO problemResponseDTO = new ProblemDTO();
        problemResponseDTO.setId(problem.getId());
        problemResponseDTO.setTitle(problem.getTitle());
        problemResponseDTO.setDescription(problem.getDescription());
        problemResponseDTO.setTimeLimit(problem.getTimeLimit());
        problemResponseDTO.setMemoryLimit(problem.getMemoryLimit());

        List<TestCaseDTO> testCaseDTOs = problem.getTestCases().stream().map(testCase -> {
            TestCaseDTO testCaseDTO = new TestCaseDTO();
            testCaseDTO.setInput(testCase.getInput());
            testCaseDTO.setExpectedOutput(testCase.getExpectedOutput());
            return testCaseDTO;
        }).collect(Collectors.toList());

        problemResponseDTO.setTestCases(testCaseDTOs);

        return problemResponseDTO;
    }
}


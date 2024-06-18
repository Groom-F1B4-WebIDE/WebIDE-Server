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
    
    /*
    private List<Problem> loadDummyProblems() {
        List<Problem> dummyProblems = new ArrayList<>();

        Problem problem1 = new Problem();
        problem1.setId(1L);
        problem1.setTitle("A + B 문제");
        problem1.setDescription("두 정수 A와 B를 입력받은 다음, A+B를 출력하는 프로그램을 작성하시오.");

        List<TestCase> testCases1 = new ArrayList<>();
        testCases1.add(new TestCase("1 2", "3"));
        testCases1.add(new TestCase("2 3", "5"));
        problem1.setTestCases(testCases1);

        Problem problem2 = new Problem();
        problem2.setId(2L);
        problem2.setTitle("최대값 찾기");
        problem2.setDescription("N개의 수가 주어졌을 때, 그 중 최댓값을 찾는 프로그램을 작성하시오.");

        List<TestCase> testCases2 = new ArrayList<>();
        testCases2.add(new TestCase("5\n20 10 35 30 7", "35"));
        testCases2.add(new TestCase("4\n1 2 3 4", "4"));
        problem2.setTestCases(testCases2);

        Problem problem3 = new Problem();
        problem3.setId(3L);
        problem3.setTitle("올바른 괄호");
        problem3.setDescription("괄호가 바르게 짝지어졌다는 것은 '(' 문자로 열렸으면 반드시 짝지어서 ')' 문자로 닫혀야 한다는 뜻입니다.");

        List<TestCase> testCases3 = new ArrayList<>();
        testCases3.add(new TestCase("()()", "true"));
        testCases3.add(new TestCase("(())()", "true"));
        testCases3.add(new TestCase(")()(", "false"));
        testCases3.add(new TestCase("(()(", "false"));
        problem3.setTestCases(testCases3);

        Problem problem4 = new Problem();
        problem4.setId(4L);
        problem4.setTitle("BOJ 15650");
        problem4.setDescription("");

        List<TestCase> testCases4 = new ArrayList<>();
        testCases4.add(new TestCase("3 1","1\n2\n3"));
        testCases4.add(new TestCase("4 2", """
                1 2
                1 3
                1 4
                2 3
                2 4
                3 4"""));
        testCases4.add(new TestCase("4 4","1 2 3 4"));
        problem4.setTestCases(testCases4);

        Problem problem5 = new Problem();
        problem5.setId(5L);
        problem5.setTitle("BOJ 1021");
        problem5.setDescription("");

        List<TestCase> testCases5 = new ArrayList<>();
        testCases5.add(new TestCase("10 3\n1 2 3", "0"));
        testCases5.add(new TestCase("10 3\n2 9 5", "8"));
        testCases5.add(new TestCase("32 6\n27 16 30 11 6 23", "59"));
        testCases5.add(new TestCase("10 10\n1 6 3 2 7 9 8 4 10 5", "14"));
        problem5.setTestCases(testCases5);

        Problem problem6 = new Problem();
        problem6.setId(6L);
        problem6.setTitle("BOJ 10773");
        problem6.setDescription("");
        problem6.setTimeLimit(2000);
        problem6.setMemoryLimit(25); // 256 MB 256 * 1024 * 1024

        List<TestCase> testCases6 = new ArrayList<>();
        testCases6.add(new TestCase("4\n3\n0\n4\n0", "0"));
        testCases6.add(new TestCase("10\n1\n3\n5\n4\n0\n0\n7\n0\n0\n6", "7"));
        problem6.setTestCases(testCases6);

        dummyProblems.add(problem1);
        dummyProblems.add(problem2);
        dummyProblems.add(problem3);
        dummyProblems.add(problem4);
        dummyProblems.add(problem5);
        dummyProblems.add(problem6);

        return dummyProblems;
    }
    */
}


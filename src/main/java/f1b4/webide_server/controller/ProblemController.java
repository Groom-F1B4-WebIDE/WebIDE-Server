package f1b4.webide_server.controller;


import f1b4.webide_server.dto.ProblemDTO;
import f1b4.webide_server.entity.code.CodeRequest;
import f1b4.webide_server.entity.code.JavaCodeUtil;
import f1b4.webide_server.entity.problem.MissionResult;
import f1b4.webide_server.entity.problem.Problem;
import f1b4.webide_server.entity.problem.TestCase;
import f1b4.webide_server.repository.MissionResultRepository;
import f1b4.webide_server.service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mission")
public class ProblemController {
    private final ProblemService problemService;

    private final MissionResultRepository resultRepository;

    @GetMapping
    public ResponseEntity<List<ProblemDTO>> getProblems() {
        List<ProblemDTO> problems = problemService.getProblems();
        return ResponseEntity.ok(problems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemDTO> getProblem(@PathVariable Long id) {
        ProblemDTO problemResponseDTO = problemService.getProblem(id);
        return ResponseEntity.ok(problemResponseDTO);
    }

    @GetMapping("/mission-results")
    public ResponseEntity<List<MissionResult>> getMissionResults() {
        List<MissionResult> results = resultRepository.findAll();
        return ResponseEntity.ok(results);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Map<String, Object>> submitSolution(@PathVariable Long id, @RequestBody CodeRequest codeRequest) {
        Problem problem = problemService.getProblemById(id);
        String code = codeRequest.getCode();
        String language = codeRequest.getLanguage().toLowerCase();
        Map<String, Object> response = new HashMap<>();

        try {
            ResponseEntity<String> compileResponse = compileCode(code, language);

            if (compileResponse == null || !compileResponse.getStatusCode().is2xxSuccessful()) {
                log.error("컴파일 실패: {}", compileResponse != null ? compileResponse.getBody() : "unknown error");
                response.put("status", "error");
                response.put("message", compileResponse != null ? compileResponse.getBody() : "컴파일 중 알 수 없는 오류 발생");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            long timeLimitMillis = problem.getTimeLimit();
            long memoryLimitBytes = problem.getMemoryLimit();

            List<Map<String, Object>> testCaseResults = runTestCases(problem.getTestCases(), language, timeLimitMillis, memoryLimitBytes);

            boolean isCorrect = testCaseResults.stream().allMatch(result -> "correct".equals(result.get("status")));
            response.put("status", "done");
            response.put("results", testCaseResults);

            // 제출 결과 저장
            MissionResult missionResult = new MissionResult();
            missionResult.setProblemId(id);
//            missionResult.setUsername(codeRequest.get); // 사용자가 로그인할 경우 필요
            missionResult.setCode(code);
            missionResult.setCorrect(isCorrect);
            missionResult.setResult(response.toString());
            missionResult.setResultTimeAt(LocalDateTime.now());
            resultRepository.save(missionResult);
            log.info("{}", missionResult);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return handleException(response, "파일 입출력 중 오류 발생", e);
        } catch (InterruptedException e) {
            return handleException(response, "프로세스 실행 중 오류 발생", e);
        } catch (Exception e) {
            return handleException(response, "실행 중 오류 발생", e);
        }
    }

    @GetMapping("/{id}/submissions")
    public List<MissionResult> getSubmissionsByProblemId(@PathVariable Long id) {
        return resultRepository.findByProblemId(id);
    }

    @GetMapping("/user/{memberEmail}/submissions")
    public List<MissionResult> getSubmissionsByUsername(@PathVariable String memberEmail) {
        return resultRepository.findByMemberEmail(memberEmail);
    }

    private ResponseEntity<String> compileCode(String code, String language) throws IOException, InterruptedException {
        switch (language) {
            case "java":
                code = JavaCodeUtil.changeClassNameToUserCode(code);
                log.info("컴파일 중인 자바 코드: {}", code);
                return compileJavaCode(code);
            case "python":
                log.info("컴파일 중인 파이썬 코드: {}", code);
                return compilePythonCode(code);
            case "cpp":
                log.info("컴파일 중인 C++ 코드: {}", code);
                return compileCppCode(code);
            default:
                throw new IllegalArgumentException("지원되지 않는 언어: " + language);
        }
    }

    private List<Map<String, Object>> runTestCases(List<TestCase> testCases, String language, long timeLimitMillis, long memoryLimitMillis) throws IOException, InterruptedException {
        List<Map<String, Object>> testCaseResults = new ArrayList<>();
        for (TestCase testCase : testCases) {
            Map<String, Object> testCaseResult = new HashMap<>();
            testCaseResult.put("input", testCase.getInput());
            testCaseResult.put("expectedOutput", testCase.getExpectedOutput());
            try {
                log.info("테스트 케이스 실행: input={}, expectedOutput={}", testCase.getInput(), testCase.getExpectedOutput());
                String userOutput = executeCode(testCase.getInput(), language, timeLimitMillis, memoryLimitMillis);
                log.info("사용자 출력: {}", userOutput);
                testCaseResult.put("userOutput", userOutput.trim());

                if (!userOutput.trim().equals(testCase.getExpectedOutput().trim())) {
                    testCaseResult.put("status", "incorrect");
                } else {
                    testCaseResult.put("status", "correct");
                }
            } catch (IOException | InterruptedException e) {
                if ("실행 시간 초과".equals(e.getMessage())) {
                    testCaseResult.put("status", "timeout");
                } else if ("메모리 초과".equals(e.getMessage())) {
                    testCaseResult.put("status", "memory 초과");
                } else {
                    testCaseResult.put("status", "error");
                    testCaseResult.put("message", e.getMessage());
                }
            }
            testCaseResults.add(testCaseResult);
        }
        return testCaseResults;
    }

    private String executeCode(String inputValues, String language, long timeoutMillis, long memoryBytes) throws IOException, InterruptedException {
        ProcessBuilder runProcessBuilder = switch (language) {
            case "java" -> new ProcessBuilder("/usr/bin/java", "UserCode");
            case "python" -> new ProcessBuilder("/usr/bin/python3", "UserCode.py");
            case "cpp" -> new ProcessBuilder("./UserCode");
            default -> throw new IllegalArgumentException("지원되지 않는 언어입니다.");
        };

        Process runProcess = runProcessBuilder.start();
        BufferedWriter processInput = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
        BufferedReader processOutput = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        BufferedReader processError = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));

        processInput.write(inputValues + "\n");
        processInput.flush();

        StringBuilder output = new StringBuilder();
        String line;
        long startTime = System.currentTimeMillis();

        while ((line = processOutput.readLine()) != null || processError.ready()) {
            if (line != null) {
                output.append(line).append("\n");
            }
            if (processError.ready()) {
                output.append("ERROR: ").append(processError.readLine()).append("\n");
            }
            if (System.currentTimeMillis() - startTime > timeoutMillis) {
                runProcess.destroy();
                throw new IOException("실행 시간 초과");
            }

            long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            if (usedMemory > memoryBytes) {
                runProcess.destroy();
                throw new IOException("메모리 제한 초과");
            }
        }
        runProcess.waitFor();
        return output.toString();
    }


    private ResponseEntity<Map<String, Object>> handleException(Map<String, Object> response, String message, Exception e) {
        log.error(message, e);
        response.put("status", "error");
        response.put("message", message + ": " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private ResponseEntity<String> compileJavaCode(String code) throws IOException, InterruptedException {
        Files.write(Paths.get("UserCode.java"), code.getBytes());

        ProcessBuilder compileBuilder = new ProcessBuilder("/usr/bin/javac", "UserCode.java");
        Process compileProcess = compileBuilder.start();
        compileProcess.waitFor();

        if (compileProcess.exitValue() != 0) {
            String error = new String(compileProcess.getErrorStream().readAllBytes());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        return ResponseEntity.ok("Java 코드 준비 완료");
    }

    private ResponseEntity<String> compilePythonCode(String code) throws IOException {
        Files.write(Paths.get("UserCode.py"), code.getBytes());
        return ResponseEntity.ok("Python 코드 준비 완료");
    }

    private ResponseEntity<String> compileCppCode(String code) throws IOException, InterruptedException {
        Files.write(Paths.get("UserCode.cpp"), code.getBytes());

        ProcessBuilder compileBuilder = new ProcessBuilder("/usr/bin/g++", "-o", "UserCode", "UserCode.cpp");
        Process compileProcess = compileBuilder.start();
        compileProcess.waitFor();

        if (compileProcess.exitValue() != 0) {
            String error = new String(compileProcess.getErrorStream().readAllBytes());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        return ResponseEntity.ok("C++ 코드 준비 완료");
    }

}

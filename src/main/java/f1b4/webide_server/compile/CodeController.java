package f1b4.webide_server.compile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/api/code")
@CrossOrigin(origins = "*")
public class CodeController {

    private Process runProcess;
    private BufferedReader processOutput;
    private BufferedWriter processInput;

    @PostMapping("/compile")
    public ResponseEntity<String> compileCode(@RequestBody CodeRequest codeRequest) {
        log.info("요청 -> : code = {}, language= {} ", codeRequest.getCode(), codeRequest.getLanguage());

        String language = codeRequest.getLanguage().toLowerCase();
        String code = codeRequest.getCode();
//        String code = JavaCodeUtil.changeClassNameToSolution(codeRequest.getCode());

        log.info("변경된 자바 {}", code);

        try {
            switch (language) {
                case "java":
                    return compileJavaCode(code);
                case "python":
                    return compilePythonCode(code);
                case "cpp":
                    return compileCppCode(code);
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("test");
            }
        } catch (Exception e) {
            log.error("api /compile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("api /compile execution error");
        }
    }

    @PostMapping("/execute")
    public ResponseEntity<String> executeCode(@RequestBody CodeRequest codeRequest) {
        log.info("api /execute 요총: {}", codeRequest.getInputValues());

        String inputValues = codeRequest.getInputValues();

        try {
            if (runProcess != null) {
                processInput.write(inputValues + "\n");
                processInput.flush();

                StringBuilder output = new StringBuilder();
                String line;
                while ((line = processOutput.readLine()) != null) {
                    output.append(line).append("\n");
                }
                return ResponseEntity.ok(output.toString());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("run 과정 x");
            }
        } catch (Exception e) {
            log.error("api /execute", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("api /execute execution error");
        }
    }

    private ResponseEntity<String> compileJavaCode(String code) throws IOException, InterruptedException {
        Files.write(Paths.get("UserCode.java"), code.getBytes());

        ProcessBuilder compileBuilder = new ProcessBuilder("javac", "UserCode.java");
        Process compileProcess = compileBuilder.start();
        compileProcess.waitFor();

        if (compileProcess.exitValue() != 0) {
            String error = new String(compileProcess.getErrorStream().readAllBytes());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        runProcess = new ProcessBuilder("java", "UserCode").start();
        processOutput = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        processInput = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
        return ResponseEntity.ok("Java 코드 준비 완료");
    }

    private ResponseEntity<String> compilePythonCode(String code) throws IOException {
        Files.write(Paths.get("UserCode.py"), code.getBytes());

        runProcess = new ProcessBuilder("python3", "UserCode.py").start();
        processOutput = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        processInput = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
        return ResponseEntity.ok("Python 코드 준비 완료");
    }

    private ResponseEntity<String> compileCppCode(String code) throws IOException, InterruptedException {
        Files.write(Paths.get("UserCode.cpp"), code.getBytes());

        ProcessBuilder compileBuilder = new ProcessBuilder("g++", "-o", "UserCode", "UserCode.cpp");
        Process compileProcess = compileBuilder.start();
        compileProcess.waitFor();

        if (compileProcess.exitValue() != 0) {
            String error = new String(compileProcess.getErrorStream().readAllBytes());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        runProcess = new ProcessBuilder("./UserCode").start();
        processOutput = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        processInput = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
        return ResponseEntity.ok("C++ 코드 준비 완료");
    }
}

package f1b4.webide_server.controller;

import f1b4.webide_server.entity.problem.Problem;
import f1b4.webide_server.dto.ProblemDTO;
import f1b4.webide_server.service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/problems")
public class ProblemCreateController {
    private final ProblemService problemService;

    @GetMapping("/{id}")
    public ResponseEntity<ProblemDTO> getProblem(@PathVariable Long id) {
        ProblemDTO problemResponseDTO = problemService.getProblem(id);
        return new ResponseEntity<>(problemResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Problem> createProblem(@RequestBody ProblemDTO problemDTO) {
        log.info("ec2 test");
        Problem problem = problemService.createProblem(problemDTO);
        return new ResponseEntity<>(problem, HttpStatus.CREATED);
    }
}

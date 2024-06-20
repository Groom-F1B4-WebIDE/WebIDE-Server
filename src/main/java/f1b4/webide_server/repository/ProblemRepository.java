package f1b4.webide_server.repository;

import f1b4.webide_server.entity.problem.Problem;
import org.springframework.data.repository.CrudRepository;

public interface ProblemRepository extends CrudRepository<Problem, Long> {
}

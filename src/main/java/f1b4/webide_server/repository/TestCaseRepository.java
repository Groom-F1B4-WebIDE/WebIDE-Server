package f1b4.webide_server.repository;

import f1b4.webide_server.domain.problem.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
}

package f1b4.webide_server.repository;

import f1b4.webide_server.entity.problem.MissionResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionResultRepository extends JpaRepository<MissionResult, Long> {
    List<MissionResult> findByProblemId(Long problemId);
    List<MissionResult> findByMemberEmail(String memberEmail);
//    List<MissionResult> findByMemberId(Long id);
}

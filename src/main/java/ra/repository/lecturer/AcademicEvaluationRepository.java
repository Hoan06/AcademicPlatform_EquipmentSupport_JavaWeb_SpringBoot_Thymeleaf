package ra.repository.lecturer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.AcademicEvaluation;

@Repository
public interface AcademicEvaluationRepository extends JpaRepository<AcademicEvaluation, Long> {
    AcademicEvaluation findBySessionId(long id);
}

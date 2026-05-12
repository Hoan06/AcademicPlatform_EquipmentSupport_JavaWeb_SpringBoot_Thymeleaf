package ra.repository.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.entity.MentoringSession;
import ra.model.enum_status.SessionStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Repository
public interface MentoringSessionRepository extends JpaRepository<MentoringSession, Long> {
    boolean existsByLecturerIdAndSessionDateAndStartTimeAndStatusNot(
            Long lecturerId,
            LocalDate sessionDate,
            LocalTime startTime,
            SessionStatus status
    );
    List<MentoringSession> findByLecturerIdAndStatus(Long lecturerId, SessionStatus status);
    List<MentoringSession> findByStudentId(Long studentId);
    @Query(value = "select distinct ms from MentoringSession ms " +
            "left join fetch ms.evaluation " +
            "left join fetch ms.borrowingRecord br " +
            "left join fetch br.details d " +
            "left join fetch d.equipment " +
            "where ms.student.id = :studentId",
            countQuery = "SELECT COUNT(ms) FROM MentoringSession ms WHERE ms.student.id = :studentId")
    Page<MentoringSession> findAllByStudentId(@Param("studentId") Long studentId, Pageable pageable);

    @Query("select ms.lecturer , count(ms.id) from MentoringSession ms where ms.status = 'COMPLETED' group by ms.lecturer order by count(ms.id) desc ")
    List<Object[]> findTopLecturersByCompleted(Pageable pageable);
}


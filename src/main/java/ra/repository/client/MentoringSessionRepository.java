package ra.repository.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.MentoringSession;
import ra.model.enum_status.SessionStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MentoringSessionRepository extends JpaRepository<MentoringSession, Long> {
    boolean existsByLecturerIdAndSessionDateAndStartTimeAndStatusNot(
            Long lecturerId,
            LocalDate sessionDate,
            LocalTime startTime,
            SessionStatus status
    );
    List<MentoringSession> findByLecturerIdAndStatus(Long lecturerId, SessionStatus status);}

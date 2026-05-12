package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.MentoringSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface MentoringSessionService {
    List<MentoringSession> getAll();
    void save(MentoringSession mentoringSession);
    void delete(MentoringSession mentoringSession);
    boolean isSlotBooked(Long lecturerId, LocalDate sessionDate, LocalTime startTime);
    List<MentoringSession> getPendingSessionsByLecturer(Long lecturerId);
    MentoringSession getSessionById(Long sessionId);
    List<MentoringSession> getMentoringSessionsByStudentId(Long studentId);
    Page<MentoringSession> getPaginatedSessions(Long studentId, Pageable pageable);
    List<Object[]> getTopLecturersByCompleted();
}

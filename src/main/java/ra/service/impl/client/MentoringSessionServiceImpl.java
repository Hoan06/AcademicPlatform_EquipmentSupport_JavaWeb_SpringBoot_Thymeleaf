package ra.service.impl.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.MentoringSession;
import ra.model.enum_status.SessionStatus;
import ra.repository.client.MentoringSessionRepository;
import ra.service.MentoringSessionService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MentoringSessionServiceImpl implements MentoringSessionService {
    @Autowired
    private MentoringSessionRepository mentoringSessionRepository;

    @Override
    public List<MentoringSession> getAll() {
        return mentoringSessionRepository.findAll();
    }

    @Override
    public void save(MentoringSession mentoringSession) {
        mentoringSessionRepository.save(mentoringSession);
    }

    @Override
    public void delete(MentoringSession mentoringSession) {
        mentoringSessionRepository.save(mentoringSession);
    }

    @Override
    public boolean isSlotBooked(Long lecturerId, LocalDate sessionDate, LocalTime startTime) {
        return mentoringSessionRepository
                .existsByLecturerIdAndSessionDateAndStartTimeAndStatusNot(
                        lecturerId,
                        sessionDate,
                        startTime,
                        SessionStatus.CANCELLED
                );
    }

    @Override
    public List<MentoringSession> getPendingSessionsByLecturer(Long lecturerId) {
        return mentoringSessionRepository.findByLecturerIdAndStatus(lecturerId, SessionStatus.PENDING);
    }

    @Override
    public MentoringSession getSessionById(Long sessionId) {
        return mentoringSessionRepository.findById(sessionId).orElse(null);
    }


}

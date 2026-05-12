package ra.service.impl.lecturer;

import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.exception.ErrorSaveEvaluation;
import ra.model.dto.AcademicEvaluationDTO;
import ra.model.dto.EquipmentDTO;
import ra.model.dto.EquipmentItemDTO;
import ra.model.entity.*;
import ra.model.enum_status.BorrowingStatus;
import ra.model.enum_status.SessionStatus;
import ra.repository.lecturer.AcademicEvaluationRepository;
import ra.service.*;

import java.util.ArrayList;

@Service
public class AcademicEvaluationServiceImpl implements AcademicEvaluationService {
    @Autowired
    private MentoringSessionService mentoringSessionService;

    @Autowired
    private LabRoomService labRoomService;

    @Autowired
    private AcademicEvaluationRepository academicEvaluationRepository;

    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @Autowired
    private BorrowingDetailService borrowingDetailService;

    @Autowired
    private EquipmentService equipmentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEvaluate(AcademicEvaluationDTO evaluationDTO) {
        try {
            MentoringSession mentoringSession = mentoringSessionService.getSessionById(evaluationDTO.getSessionId());
            mentoringSession.setStatus(SessionStatus.COMPLETED);
            mentoringSessionService.save(mentoringSession);

            AcademicEvaluation academicEvaluation = new  AcademicEvaluation();
            academicEvaluation.setSession(mentoringSession);
            academicEvaluation.setComment(evaluationDTO.getComment());
            academicEvaluation.setPerformance(determinePerformance(evaluationDTO.getPerformance()));
            academicEvaluation.setLabRoom(labRoomService.getById(evaluationDTO.getLabRoomId()));
            academicEvaluationRepository.save(academicEvaluation);

            BorrowingRecord borrowingRecord = new BorrowingRecord();
            borrowingRecord.setSession(mentoringSession);
            borrowingRecord.setStatus(BorrowingStatus.PENDING);
            borrowingRecord.setDetails(new ArrayList<>());

            if (evaluationDTO.getEquipments() != null) {
                for (EquipmentItemDTO item : evaluationDTO.getEquipments()) {
                    BorrowingDetail detail = new BorrowingDetail();
                    detail.setBorrowingRecord(borrowingRecord);
                    detail.setEquipment(equipmentService.findById(item.getEquipmentId()));
                    detail.setQuantity(item.getQuantity());

                    borrowingRecord.getDetails().add(detail);
                }
            }

            borrowingRecordService.save(borrowingRecord);

        } catch (Exception e){
            throw new ErrorSaveEvaluation(e.getMessage());
        }
    }

    @Override
    public AcademicEvaluation getEvaluateBySessionId(Long sessionId) {
        return academicEvaluationRepository.findBySessionId(sessionId);
    }

    private String determinePerformance(double score) {
        if (score >= 90) return "Xuất sắc";
        if (score >= 80) return "Giỏi";
        if (score >= 60) return "Khá";
        if (score >= 40) return "Trung bình";
        return "Yếu";
    }
}

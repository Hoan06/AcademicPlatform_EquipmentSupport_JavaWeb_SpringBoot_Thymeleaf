package ra.service;

import ra.model.dto.AcademicEvaluationDTO;
import ra.model.entity.AcademicEvaluation;

public interface AcademicEvaluationService {
    void saveEvaluate(AcademicEvaluationDTO evaluationDTO);
    AcademicEvaluation getEvaluateBySessionId(Long sessionId);
}

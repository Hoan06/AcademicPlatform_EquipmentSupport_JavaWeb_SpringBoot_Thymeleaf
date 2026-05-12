package ra.service.impl.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.BorrowingRecord;
import ra.model.enum_status.BorrowingStatus;
import ra.repository.lecturer.BorrowingRecordRepository;
import ra.service.BorrowingRecordService;

import java.util.List;

@Service
public class BorrowingRecordServiceImpl implements BorrowingRecordService {
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Override
    public List<BorrowingRecord> getAll() {
        return borrowingRecordRepository.findAll();
    }

    @Override
    public void save(BorrowingRecord borrowingRecord) {
        borrowingRecordRepository.save(borrowingRecord);
    }

    @Override
    public void update(BorrowingRecord borrowingRecord) {
        borrowingRecordRepository.save(borrowingRecord);
    }

    @Override
    public BorrowingRecord getById(long id) {
        return borrowingRecordRepository.findById(id).orElse(null);
    }

    @Override
    public List<BorrowingRecord> getBorrowingRecordsByStatusPending() {
        return borrowingRecordRepository.findByStatus(BorrowingStatus.PENDING);
    }

    @Override
    public BorrowingRecord getBorrowingRecordBySessionId(long sessionId) {
        return borrowingRecordRepository.findBySessionId(sessionId);
    }
}

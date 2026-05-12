package ra.service;

import ra.model.entity.BorrowingRecord;

import java.util.List;

public interface BorrowingRecordService {
    List<BorrowingRecord> getAll();
    void save(BorrowingRecord borrowingRecord);
    void update(BorrowingRecord borrowingRecord);
    BorrowingRecord getById(long id);
    List<BorrowingRecord> getBorrowingRecordsByStatusPending();
    BorrowingRecord getBorrowingRecordBySessionId(long sessionId);
}

package ra.repository.lecturer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.model.entity.BorrowingRecord;
import ra.model.enum_status.BorrowingStatus;

import java.util.List;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord,Long> {
    List<BorrowingRecord> findByStatus(BorrowingStatus status);
    BorrowingRecord findBySessionId(Long sessionId);


}

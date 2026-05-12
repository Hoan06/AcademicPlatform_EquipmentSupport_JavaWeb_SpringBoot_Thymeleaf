package ra.repository.lecturer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.model.entity.BorrowingDetail;

import java.util.List;

@Repository
public interface BorrowingDetailRepository extends JpaRepository<BorrowingDetail, Long> {
    List<BorrowingDetail> findBorrowingDetailsByBorrowingRecordId(long id);

    @Query("select coalesce(sum(b.quantity) , 0) from BorrowingDetail b where b.borrowingRecord.status = 'COMPLETED'")
    Long countEquipmentsBorrowing();

    @Query("select coalesce(sum(b.quantity) , 0) from BorrowingDetail b where b.borrowingRecord.status = 'PENDING'")
    Long countEquipmentPending();

    @Query("select b.equipment , coalesce(sum(b.quantity) , 0) from BorrowingDetail b where b.borrowingRecord.status = 'COMPLETED' and b.equipment.isDelete = false group by b.equipment order by sum(b.quantity) desc ")
    List<Object[]> findTopEquipmentMostBorrowing(Pageable pageable);
}

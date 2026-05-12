package ra.service;

import ra.model.entity.BorrowingDetail;

import java.util.List;

public interface BorrowingDetailService {
    void saveBorrowingDetail(BorrowingDetail borrowingDetail);
    List<BorrowingDetail> findAllBorrowingDetailByRecordId(long id);
    Long getCountEquipmentsBorrowing();
    Long getCountEquipmentPending();
    List<Object[]> getTopEquipmentMostBorrowing();
}

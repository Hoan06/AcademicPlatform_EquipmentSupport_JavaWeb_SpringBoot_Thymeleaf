package ra.service.impl.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ra.model.entity.BorrowingDetail;
import ra.repository.lecturer.BorrowingDetailRepository;
import ra.service.BorrowingDetailService;

import java.util.List;

@Service
public class BorrowingDetailServiceImpl implements BorrowingDetailService {
    @Autowired
    private BorrowingDetailRepository borrowingDetailRepository;

    @Override
    public void saveBorrowingDetail(BorrowingDetail borrowingDetail) {
        borrowingDetailRepository.save(borrowingDetail);
    }

    @Override
    public List<BorrowingDetail> findAllBorrowingDetailByRecordId(long id) {
        return borrowingDetailRepository.findBorrowingDetailsByBorrowingRecordId(id);
    }

    @Override
    public Long getCountEquipmentsBorrowing() {
        return borrowingDetailRepository.countEquipmentsBorrowing();
    }

    @Override
    public Long getCountEquipmentPending() {
        return borrowingDetailRepository.countEquipmentPending();
    }

    @Override
    public List<Object[]> getTopEquipmentMostBorrowing() {
        return borrowingDetailRepository.findTopEquipmentMostBorrowing(PageRequest.of(0, 3));
    }
}

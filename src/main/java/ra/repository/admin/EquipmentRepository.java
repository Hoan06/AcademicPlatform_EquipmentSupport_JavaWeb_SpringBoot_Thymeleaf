package ra.repository.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.model.entity.Equipment;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Page<Equipment> findAllByIsDeleteFalse(Pageable pageable);
    List<Equipment> findAllByIsDeleteFalse();
    Page<Equipment> findByIsDeleteFalseAndNameContainingIgnoreCaseAndCategoryNameContaining(
            String name, String categoryName, Pageable pageable);

    @Query("select coalesce(sum(e.quantity), 0) from Equipment e where e.isDelete = false")
    Long totalEquipmentsInSystem();

    @Query("select count(e.id) from Equipment e where e.isDelete = false and e.available < 5")
    Long countLowStockEquipments();
}

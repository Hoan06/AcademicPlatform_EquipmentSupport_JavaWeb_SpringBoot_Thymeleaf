package ra.service.impl.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.Equipment;
import ra.repository.admin.EquipmentRepository;
import ra.service.EquipmentService;

import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public Page<Equipment> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return equipmentRepository.findAllByIsDeleteFalse(pageable);
    }

    @Override
    public Equipment findById(Long id) {
        return equipmentRepository.findById(id).get();
    }

    @Override
    public Equipment save(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment update(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @Override
    public void delete(Long id) {
        equipmentRepository.deleteById(id);
    }

    @Override
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAllByIsDeleteFalse();
    }

    @Override
    public Page<Equipment> searchEquipments(String search, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        String categoryFilter = (category == null || category.equals("Tất cả")) ? "" : category;
        return equipmentRepository.findByIsDeleteFalseAndNameContainingIgnoreCaseAndCategoryNameContaining(
                search, categoryFilter, pageable);
    }

    @Override
    public Long getTotalEquipmentsInSystem() {
        return equipmentRepository.totalEquipmentsInSystem();
    }

    @Override
    public Long getCountLowStockEquipments() {
        return equipmentRepository.countLowStockEquipments();
    }
}

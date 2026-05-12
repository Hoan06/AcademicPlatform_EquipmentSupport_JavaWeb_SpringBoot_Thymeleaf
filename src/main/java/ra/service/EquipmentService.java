package ra.service;

import org.springframework.data.domain.Page;
import ra.model.entity.Equipment;

import java.util.List;

public interface EquipmentService {
    Page<Equipment> findAll(Integer page, Integer size);
    Equipment findById(Long id);
    Equipment save(Equipment equipment);
    Equipment update(Equipment equipment);
    void delete(Long id);
    List<Equipment> getAllEquipments();
    Page<Equipment> searchEquipments(String search, String category, int page, int size);
    Long getTotalEquipmentsInSystem();
    Long getCountLowStockEquipments();
}

package ra.service;

import org.springframework.data.domain.Page;
import ra.model.entity.Equipment;

public interface EquipmentService {
    Page<Equipment> findAll(Integer page, Integer size);
    Equipment findById(Long id);
    Equipment save(Equipment equipment);
    Equipment update(Equipment equipment);
    void delete(Long id);
}

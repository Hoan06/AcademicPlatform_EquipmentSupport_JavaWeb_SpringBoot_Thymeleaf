package ra.service;

import ra.model.entity.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getAlls();
    Department findById(Long id);
}

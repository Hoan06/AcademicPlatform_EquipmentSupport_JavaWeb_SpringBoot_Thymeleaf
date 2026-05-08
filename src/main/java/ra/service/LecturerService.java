package ra.service;

import ra.model.entity.Lecturer;

import java.util.List;

public interface LecturerService {
    List<Lecturer> getAll();
    void save(Lecturer lecturer);
    List<Lecturer> findByDepartmentId(Long departmentId);
}

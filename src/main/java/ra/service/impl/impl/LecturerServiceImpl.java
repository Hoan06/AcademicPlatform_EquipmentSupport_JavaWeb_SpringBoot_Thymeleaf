package ra.service.impl.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Lecturer;
import ra.repository.lecturer.LecturerRepository;
import ra.service.LecturerService;

import java.util.List;

@Service
public class LecturerServiceImpl implements LecturerService {
    @Autowired
    private LecturerRepository lecturerRepository;

    @Override
    public List<Lecturer> getAll() {
        return lecturerRepository.findAll();
    }

    @Override
    public void save(Lecturer lecturer) {
        lecturerRepository.save(lecturer);
    }

    @Override
    public List<Lecturer> findByDepartmentId(Long departmentId) {
        return lecturerRepository.findAllByDepartmentId(departmentId);
    }

}

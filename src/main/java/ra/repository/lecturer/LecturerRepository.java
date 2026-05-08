package ra.repository.lecturer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Lecturer;

import java.util.List;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    List<Lecturer> findAllByDepartmentId(Long departmentId);
}

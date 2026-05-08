package ra.repository.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}

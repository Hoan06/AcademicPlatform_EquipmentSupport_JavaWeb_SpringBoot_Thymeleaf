package ra.repository.lecturer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.LabRoom;

@Repository
public interface LabRoomRepository extends JpaRepository<LabRoom, Long> {
}

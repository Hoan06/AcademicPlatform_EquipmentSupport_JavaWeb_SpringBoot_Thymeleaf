package ra.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
    UserProfile findByUser_Id(Long id);
}

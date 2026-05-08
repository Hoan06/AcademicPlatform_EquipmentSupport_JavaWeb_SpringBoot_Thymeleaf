package ra.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User  findByUsername(String username);
    boolean existsByRole(String role);
}

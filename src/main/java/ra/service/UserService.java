package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.dto.LoginDTO;
import ra.model.entity.User;

public interface UserService {
    User save(User user);
    void update(User user);
    void delete(Long id);
    User findByUsername(String username);
    User login(LoginDTO loginDTO);
    boolean existsByRole(String role);
    Page<User> findAll(Integer page, Integer size);
    User findById(Long id);
}

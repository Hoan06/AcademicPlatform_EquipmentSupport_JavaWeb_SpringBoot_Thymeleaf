package ra.service.impl.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.dto.LoginDTO;
import ra.model.entity.User;
import ra.repository.auth.UserRepository;
import ra.service.UserService;
import ra.util.PasswordUtil;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User login(LoginDTO loginDTO) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(loginDTO.getUsername()));
        if (user.isPresent()) {
            User user1 = user.get();
            if (PasswordUtil.verify(loginDTO.getPassword() , user.get().getPassword()) && loginDTO.getRole().equals(user1.getRole())) {
                return user1;
            }
        }
        return null;
    }

    @Override
    public boolean existsByRole(String role) {
        return userRepository.existsByRole(role);
    }

    @Override
    public Page<User> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userRepository.findAll(pageable);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}

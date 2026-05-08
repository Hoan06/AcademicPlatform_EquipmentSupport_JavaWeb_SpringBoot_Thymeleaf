package ra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ra.model.entity.Department;
import ra.model.entity.User;
import ra.model.entity.UserProfile;
import ra.repository.client.DepartmentRepository;
import ra.service.UserService;
import ra.util.PasswordUtil;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeding implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) throws Exception {
        seedAdmin();
        seedDepartments();
    }

    private void seedAdmin() {
        if (!userService.existsByRole("admin")) {
            User user = new User();
            UserProfile profile = new UserProfile();

            user.setUsername("admin");
            user.setPassword(PasswordUtil.hash("admin123"));
            user.setRole("admin");
            user.setBan(false);

            user.setProfile(profile);
            profile.setUser(user);
            profile.setFullName("Administrator");

            userService.save(user);
        }
    }

    private void seedDepartments() {
        if (departmentRepository.count() == 0) {
            List<Department> departments = Arrays.asList(
                    new Department(null, "Công nghệ phần mềm", null),
                    new Department(null, "Khoa học máy tính", null),
                    new Department(null, "Hệ thống thông tin", null),
                    new Department(null, "An toàn thông tin", null)
            );

            departmentRepository.saveAll(departments);
        }
    }
}
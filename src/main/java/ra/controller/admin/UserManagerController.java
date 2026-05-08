package ra.controller.admin;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.model.dto.EquipmentDTO;
import ra.model.dto.LecturerRegisterDTO;
import ra.model.dto.RegisterDTO;
import ra.model.dto.UserDTO;
import ra.model.entity.Lecturer;
import ra.model.entity.User;
import ra.model.entity.UserProfile;
import ra.service.DepartmentService;
import ra.service.LecturerService;
import ra.service.UserService;
import ra.util.PasswordUtil;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserManagerController {
    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private LecturerService lecturerService;

    @GetMapping("/users")
    public String users(@RequestParam(defaultValue = "1") Integer page, Model model , HttpSession session) {
        if (session.getAttribute("userLogin") == null) {
            return "redirect:/auth/login";
        }
        Object user = session.getAttribute("userLogin");
        Object role = session.getAttribute("role");
        if(role != null && role.equals("user")){
            return "redirect:/client/home";
        } else if(role != null && role.equals("lecturer")){
            return "redirect:/lecturer/home";
        }

        if (!model.containsAttribute("userDto")) {
            model.addAttribute("userDto", new LecturerRegisterDTO());
        }
        model.addAttribute("departments", departmentService.getAlls());

        int size = 5;
        Page<User> users = userService.findAll(page,size);
        model.addAttribute("users", users.getContent());
        model.addAttribute("userLogin", user.toString());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());

        return "/admin/user_manager";
    }


    @PostMapping("/users/add")
    public String addUser(@Valid @ModelAttribute("userDto") LecturerRegisterDTO lecturerRegisterDTO , BindingResult result, RedirectAttributes redirectAttributes, HttpSession session , @RequestParam(defaultValue = "1") Integer page){
        if ("lecturer".equals(lecturerRegisterDTO.getRole()) && lecturerRegisterDTO.getDepartmentId() == null) {
            result.rejectValue("departmentId", "error.departmentId", "Giảng viên bắt buộc phải thuộc một khoa!");
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userDto", result);
            redirectAttributes.addFlashAttribute("userDto", lecturerRegisterDTO);
            redirectAttributes.addFlashAttribute("openModal", true);
            return "redirect:/admin/users?page=" + page;
        }
        User user = new User();
        UserProfile userProfile = new UserProfile();
        user.setUsername(lecturerRegisterDTO.getUsername());
        user.setPassword(PasswordUtil.hash(lecturerRegisterDTO.getPassword()));
        user.setRole(lecturerRegisterDTO.getRole());
        user.setBan(false);
        user.setProfile(userProfile);
        userProfile.setUser(user);
        userProfile.setFullName(lecturerRegisterDTO.getFullName());
        User savedUser = userService.save(user);

        if ("lecturer".equals(lecturerRegisterDTO.getRole()) && lecturerRegisterDTO.getDepartmentId() != null) {
            Lecturer lecturer = new Lecturer();
            lecturer.setUser(savedUser);
            lecturer.setDepartment(departmentService.findById(lecturerRegisterDTO.getDepartmentId()));
            lecturerService.save(lecturer);
        }
        redirectAttributes.addFlashAttribute("success", "Thêm tài khoản thành công");
        return  "redirect:/admin/users";
    }

    @GetMapping("/ban/{id}")
    public String ban(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, HttpSession session){
        User user = userService.findById(id);
        if (user.isBan()) {
            redirectAttributes.addFlashAttribute("success", "Tài khoản đã khóa thành công từ trước rồi !");
            return "redirect:/admin/users";
        }
        user.setBan(true);
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "Khóa tài khoản thành công");
        return "redirect:/admin/users";
    }

}

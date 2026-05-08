package ra.controller.auth;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.model.dto.LoginDTO;
import ra.model.dto.RegisterDTO;
import ra.model.entity.User;
import ra.model.entity.UserProfile;
import ra.service.UserProfileService;
import ra.service.UserService;
import ra.util.PasswordUtil;

import java.util.Optional;

@Controller
@RequestMapping({"/","/auth"})
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    private String checkLoginRedirect(HttpSession session) {
        if (session.getAttribute("userLogin") != null) {
            String role = session.getAttribute("role").toString();
            switch (role) {
                case "admin":
                    return "redirect:/admin/equipments";
                case "user":
                    return "redirect:/client/profile";
                default:
                    return "redirect:/lecturer";
            }
        }
        return null;
    }

    @GetMapping("/login")
    public String login(Model model , HttpSession session) {
        String redirect = checkLoginRedirect(session);
        if (redirect != null) return redirect;
        model.addAttribute("user" , new LoginDTO());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") LoginDTO loginDTO , BindingResult result , RedirectAttributes redirectAttributes, HttpSession session){
        String redirect = checkLoginRedirect(session);
        if (redirect != null) return redirect;
        if (result.hasErrors()){
            return "auth/login";
        }
        Optional<User> user = Optional.ofNullable(userService.login(loginDTO));
        if (user.isPresent()) {
            if (user.get().isBan()){
                redirectAttributes.addFlashAttribute("error" , "Tài khoản đã bị khóa không thể đăng nhập !");
                return "redirect:/auth/login";
            }
            session.setAttribute("userLogin", user.get().getUsername());
            session.setAttribute("role", user.get().getRole());
            if (user.get().getRole().equals("admin")) {
                return "redirect:/admin/equipments";
            } else if (user.get().getRole().equals("user")) {
                return "redirect:/client/profile";
            } else {
                return "redirect:/lecturer/dashboard";
            }
        } else {
            redirectAttributes.addFlashAttribute("error" , "Thông tin đăng nhập không chính xác !");
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/register")
    public String register(Model model , HttpSession session) {
        String redirect = checkLoginRedirect(session);
        if (redirect != null) return redirect;
        model.addAttribute("user" , new RegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RegisterDTO registerDTO , BindingResult result , RedirectAttributes redirectAttributes , HttpSession session) {
        if  (result.hasErrors()) {
            return "auth/register";
        }
        User user = new User();
        UserProfile  userProfile = new UserProfile();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(PasswordUtil.hash(registerDTO.getPassword()));
        user.setRole("user");
        user.setBan(false);
        user.setProfile(userProfile);
        userProfile.setUser(user);
        userProfile.setFullName(registerDTO.getFullName());
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "Đăng ký tài khoản thành công!");
        return "redirect:/auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userLogin");
        session.removeAttribute("role");
        return "redirect:/auth/login";
    }
}

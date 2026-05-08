package ra.controller.lecturer;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ra.model.dto.UserDTO;
import ra.model.entity.MentoringSession;
import ra.model.entity.User;
import ra.model.entity.UserProfile;
import ra.service.MentoringSessionService;
import ra.service.UserProfileService;
import ra.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private MentoringSessionService mentoringSessionService;

    @GetMapping("/dashboard")
    public String lecturerDashboard(@RequestParam(required = false) Long sessionId, Model model, HttpSession session) {
        String username = (String) session.getAttribute("userLogin");
        if (username == null) return "redirect:/auth/login";

        User userCurrent = userService.findByUsername(username);
        UserProfile userProfile = userProfileService.findByUserId(userCurrent.getId());

        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(userProfile != null ? userProfile.getFullName() : "Người dùng");
        userDTO.setUsername(username);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("userImage", userProfile != null ? userProfile.getImageUrl() : "");

        List<MentoringSession> pendingSessions = mentoringSessionService.getPendingSessionsByLecturer(userCurrent.getId());
        model.addAttribute("pendingSessions", pendingSessions);

        if (sessionId != null) {
            MentoringSession activeSession = mentoringSessionService.getSessionById(sessionId);
            model.addAttribute("activeSession", activeSession);
        }

        return "lecturer/dashboard";
    }
}

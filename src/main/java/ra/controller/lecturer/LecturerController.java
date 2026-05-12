package ra.controller.lecturer;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.model.dto.AcademicEvaluationDTO;
import ra.model.dto.UserDTO;
import ra.model.entity.MentoringSession;
import ra.model.entity.User;
import ra.model.entity.UserProfile;
import ra.service.*;
import ra.service.impl.cloud.UploadService;

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

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private LabRoomService labRoomService;

    @Autowired
    private AcademicEvaluationService academicEvaluationService;

    @Autowired
    private UploadService uploadService;

    @GetMapping("/dashboard")
    public String lecturerDashboard(@RequestParam(required = false) Long sessionId, Model model, HttpSession session) {
        String username = (String) session.getAttribute("userLogin");
        if (username == null) return "redirect:/auth/login";
        Object role = session.getAttribute("role");
        if(role != null && role.equals("user")){
            return "redirect:/client/profile";
        } else if(role != null && role.equals("admin")){
            return "redirect:/admin/equipments";
        }

        if (!model.containsAttribute("academicEvaluationDTO")) {
            model.addAttribute("academicEvaluationDTO", new AcademicEvaluationDTO());
        }

        User userCurrent = userService.findByUsername(username);
        UserProfile userProfile = userProfileService.findByUserId(userCurrent.getId());

        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(userProfile != null ? userProfile.getFullName() : "Người dùng");
        userDTO.setUsername(username);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("userImage", userProfile != null ? userProfile.getImageUrl() : "");

        List<MentoringSession> pendingSessions = mentoringSessionService.getPendingSessionsByLecturer(userCurrent.getId());
        model.addAttribute("pendingSessions", pendingSessions);
        model.addAttribute("equipments", equipmentService.getAllEquipments());

        if (sessionId != null) {
            MentoringSession activeSession = mentoringSessionService.getSessionById(sessionId);
            model.addAttribute("activeSession", activeSession);
            model.addAttribute("labRooms" , labRoomService.getAll());
        }

        return "lecturer/dashboard";
    }

    @PostMapping("/dashboard")
    public String addEvaluation(@Valid @ModelAttribute("academicEvaluationDTO") AcademicEvaluationDTO academicEvaluationDTO, BindingResult bindingResult,@RequestParam Long sessionId , RedirectAttributes redirectAttributes, HttpSession session) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.academicEvaluationDTO", bindingResult);
            redirectAttributes.addFlashAttribute("academicEvaluationDTO", academicEvaluationDTO);
            return "redirect:/lecturer/dashboard?sessionId=" + sessionId;
        }

        try {
            academicEvaluationService.saveEvaluate(academicEvaluationDTO);

            redirectAttributes.addFlashAttribute("success", "Đã lưu đánh giá và tạo phiếu mượn thiết bị thành công!");
            return "redirect:/lecturer/dashboard";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lưu thất bại: " + e.getMessage());
            redirectAttributes.addFlashAttribute("academicEvaluationDTO", academicEvaluationDTO);
            return "redirect:/lecturer/dashboard?sessionId=" + sessionId;
        }
    }

    @GetMapping("/profileLecturer")
    public String profile(Model model, HttpSession session) {
        String username = (String) session.getAttribute("userLogin");
        if (username == null) {
            return "redirect:/auth/login";
        }

        User userCurrent = userService.findByUsername(username);
        if (userCurrent == null) {
            session.invalidate();
            return "redirect:/auth/login";
        }

        String role = (String) session.getAttribute("role");
        if ("admin".equals(role)) {
            return "redirect:/admin/equipments";
        } else if ("user".equals(role)) {
            return "redirect:/client/profile";
        }

        UserProfile userProfile = userProfileService.findByUserId(userCurrent.getId());

        if (userProfile == null) {
            userProfile = new UserProfile();
            userProfile.setFullName("Chưa cập nhật");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userCurrent.getId());
        userDTO.setUsername(userCurrent.getUsername());
        userDTO.setRole(userCurrent.getRole());
        userDTO.setFullName(userProfile.getFullName());

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("userImage", userProfile.getImageUrl());

        return "lecturer/profile_lecturer";
    }

    @PostMapping("/updateLecturer")
    public String updateProfile(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "redirect:/lecturer/profileLecturer";
        }
        UserProfile userProfile = userProfileService.findByUserId(userDTO.getId());
        userProfile.setFullName(userDTO.getFullName());
        if (userDTO.getImageFile() != null && !userDTO.getImageFile().isEmpty()) {
            String newImageUrl = uploadService.uploadImage(userDTO.getImageFile());
            if (newImageUrl != null) {
                userProfile.setImageUrl(newImageUrl);
            }
        }
        userProfileService.save(userProfile);

        return "redirect:/lecturer/profileLecturer";
    }

}

package ra.controller.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ra.model.dto.AcademicEvaluationDTO;
import ra.model.dto.UserDTO;
import ra.model.entity.BorrowingRecord;
import ra.model.entity.User;
import ra.model.entity.UserProfile;
import ra.service.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class DashboardAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @Autowired
    private BorrowingDetailService borrowingDetailService;

    @Autowired
    private MentoringSessionService mentoringSessionService;

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/dashboardAdmin")
    public String dashboardAdmin(Model model , HttpSession session) {
        String username = (String) session.getAttribute("userLogin");

        User userCurrent = userService.findByUsername(username);
        UserProfile userProfile = userProfileService.findByUserId(userCurrent.getId());

        model.addAttribute("userLogin", username);
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(userProfile != null ? userProfile.getFullName() : "Người dùng");
        userDTO.setUsername(username);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("userImage", userProfile != null ? userProfile.getImageUrl() : "");

        List<BorrowingRecord> pendingRecords = borrowingRecordService.getBorrowingRecordsByStatusPending();

        if (pendingRecords == null) {
            pendingRecords = new ArrayList<>();
        }
        model.addAttribute("pendingRecords", pendingRecords);

        Long totalEquipmentBorrowing = borrowingDetailService.getCountEquipmentsBorrowing();
        model.addAttribute("totalEquipmentBorrowing", totalEquipmentBorrowing);
        Long totalEquipmentPending = borrowingDetailService.getCountEquipmentPending();
        model.addAttribute("totalEquipmentPending", totalEquipmentPending);
        Long totalEquipmentInSystem = equipmentService.getTotalEquipmentsInSystem();
        model.addAttribute("totalEquipmentInSystem", totalEquipmentInSystem);
        Long totalEquipmentStockDanger = equipmentService.getCountLowStockEquipments();
        model.addAttribute("totalEquipmentStockDanger", totalEquipmentStockDanger);

        List<Object[]> topLecturers = mentoringSessionService.getTopLecturersByCompleted();
        List<Object[]> topEquipments = borrowingDetailService.getTopEquipmentMostBorrowing();

        model.addAttribute("topLecturers", topLecturers);
        model.addAttribute("topEquipments", topEquipments);

        return "admin/dashboard_admin";
    }
}

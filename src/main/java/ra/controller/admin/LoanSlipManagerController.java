package ra.controller.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.exception.DeviceDoesNotExist;
import ra.exception.QuantityIsNotEnough;
import ra.model.dto.AcademicEvaluationDTO;
import ra.model.dto.UserDTO;
import ra.model.entity.*;
import ra.model.enum_status.BorrowingStatus;
import ra.service.BorrowingRecordService;
import ra.service.EquipmentService;
import ra.service.UserProfileService;
import ra.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class LoanSlipManagerController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/loanSlip")
    public String loanSlip(Model model , HttpSession session , @RequestParam(required = false) Long recordId){
        String username = (String) session.getAttribute("userLogin");

        if (!model.containsAttribute("academicEvaluationDTO")) {
            model.addAttribute("academicEvaluationDTO", new AcademicEvaluationDTO());
        }

        User userCurrent = userService.findByUsername(username);
        UserProfile userProfile = userProfileService.findByUserId(userCurrent.getId());

        model.addAttribute("userLogin", username);
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(userProfile != null ? userProfile.getFullName() : "Người dùng");
        userDTO.setUsername(username);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("userImage", userProfile != null ? userProfile.getImageUrl() : "");


        List<BorrowingRecord> records = borrowingRecordService.getBorrowingRecordsByStatusPending();
        model.addAttribute("pendingRecords", records);

        if (recordId != null) {
            BorrowingRecord activeRecord = borrowingRecordService.getById(recordId);
            model.addAttribute("activeRecord", activeRecord);
        }
        return "admin/loan_slip";
    }

    @PostMapping("/loanSlip/confirm")
    public String loanSlipConfirm(@RequestParam Long recordId, RedirectAttributes redirectAttributes){
        try {
            BorrowingRecord record = borrowingRecordService.getById(recordId);
            if (record == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy phiếu mượn!");
                return "redirect:/admin/loanSlip";
            }

            for (BorrowingDetail detail : record.getDetails()) {
                Equipment equipment = detail.getEquipment();
                if (equipment.isDelete()){
                    throw new DeviceDoesNotExist("Thiết bị : " + equipment.getName() + " đã bị xóa , không thể duyệt !");
                }
                if (equipment.getAvailable() < detail.getQuantity()) {
                    throw new QuantityIsNotEnough("Không đủ số lượng tồn kho!");
                }
            }

            for (BorrowingDetail detail : record.getDetails()) {
                Equipment equipment = detail.getEquipment();
                equipment.setAvailable(equipment.getAvailable() - detail.getQuantity());
                equipmentService.save(equipment);
            }

            record.setStatus(BorrowingStatus.COMPLETED);
            borrowingRecordService.save(record);
            redirectAttributes.addFlashAttribute("success", "Xác nhận xuất kho thành công!");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/admin/loanSlip";
    }

    @PostMapping("/loanSlip/reject")
    public String rejectLoanSlip(@RequestParam Long recordId, RedirectAttributes redirectAttributes) {
        BorrowingRecord record = borrowingRecordService.getById(recordId);
        if (record != null) {
            record.setStatus(BorrowingStatus.REJECTED);
            borrowingRecordService.save(record);
            redirectAttributes.addFlashAttribute("success", "Đã từ chối yêu cầu mượn.");
        }
        return "redirect:/admin/loanSlip";
    }
}

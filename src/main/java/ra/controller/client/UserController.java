package ra.controller.client;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.model.dto.BookingDTO;
import ra.model.dto.UserDTO;
import ra.model.entity.MentoringSession;
import ra.model.entity.User;
import ra.model.entity.UserProfile;
import ra.model.enum_status.SessionStatus;
import ra.service.*;
import ra.service.impl.client.MentoringSessionServiceImpl;
import ra.service.impl.cloud.UploadService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/client")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private MentoringSessionService  mentoringSessionService;

    @GetMapping("/profile")
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
        } else if ("lecturer".equals(role)) {
            return "redirect:/lecturer/dashboard";
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

        return "client/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "redirect:/client/profile";
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

        return "redirect:/client/profile";
    }

    @GetMapping("/booking")
    public String showBookingForm(Model model, HttpSession session) {
        String username = (String) session.getAttribute("userLogin");
        if (username == null) return "redirect:/auth/login";

        String role = (String) session.getAttribute("role");
        if ("admin".equals(role)) {
            return "redirect:/admin/equipments";
        } else if ("lecturer".equals(role)) {
            return "redirect:/lecturer/dashboard";
        }

        User userCurrent = userService.findByUsername(username);
        UserProfile userProfile = userProfileService.findByUserId(userCurrent.getId());

        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(userProfile != null ? userProfile.getFullName() : "Người dùng");
        userDTO.setUsername(username);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("userImage", userProfile != null ? userProfile.getImageUrl() : "");

        if (!model.containsAttribute("bookingDTO")) {
            model.addAttribute("bookingDTO", new BookingDTO());
        }

        model.addAttribute("departments", departmentService.getAlls());
        return "client/booking";
    }


    @PostMapping("/booking")
    public String processBooking(@Valid @ModelAttribute("bookingDTO") BookingDTO bookingDTO, BindingResult result,
                                 @RequestParam(required = false) String action,
                                 RedirectAttributes redirectAttributes, Model model, HttpSession session) {
        String username = (String) session.getAttribute("userLogin");
        if (username == null) {
            return "redirect:/auth/login";
        }
        User userCurrent = userService.findByUsername(username);
        if (userCurrent == null) {
            session.invalidate();
            return "redirect:/auth/login";
        }

        UserProfile userProfile = userProfileService.findByUserId(userCurrent.getId());
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(userProfile != null ? userProfile.getFullName() : "Người dùng");
        userDTO.setUsername(username);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("userImage", userProfile != null ? userProfile.getImageUrl() : "");

        if ("refresh".equals(action)) {
            model.addAttribute("bookingDTO", bookingDTO);
            model.addAttribute("departments", departmentService.getAlls());
            if (bookingDTO.getDepartmentId() != null) {
                model.addAttribute("lecturers", lecturerService.findByDepartmentId(bookingDTO.getDepartmentId()));
            }
            model.asMap().remove("org.springframework.validation.BindingResult.bookingDTO");
            return "client/booking";
        }

        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAlls());
            if (bookingDTO.getDepartmentId() != null) {
                model.addAttribute("lecturers", lecturerService.findByDepartmentId(bookingDTO.getDepartmentId()));
            }
            return "client/booking";
        }

        int startHour = bookingDTO.getTimeSlotId().intValue();
        LocalTime startTime = LocalTime.of(startHour, 0);
        LocalTime endTime = startTime.plusHours(1);

        if (mentoringSessionService.isSlotBooked(
                bookingDTO.getTeacherId(),
                bookingDTO.getBookingDate(),
                startTime
        )) {
            model.addAttribute("error", "Giảng viên này đã có lịch trong khung giờ đã chọn. Vui lòng chọn khung giờ khác!");
            model.addAttribute("bookingDTO", bookingDTO);
            model.addAttribute("departments", departmentService.getAlls());
            if (bookingDTO.getDepartmentId() != null) {
                model.addAttribute("lecturers", lecturerService.findByDepartmentId(bookingDTO.getDepartmentId()));
            }
            return "client/booking";
        }

        try {
            User user = userService.findByUsername(username);
            User lecturer = userService.findById(bookingDTO.getTeacherId());

            MentoringSession mentoringSession = new MentoringSession();
            mentoringSession.setSessionDate(bookingDTO.getBookingDate());
            mentoringSession.setStartTime(startTime);
            mentoringSession.setEndTime(endTime);
            mentoringSession.setNote(bookingDTO.getNote());
            mentoringSession.setStudent(user);
            mentoringSession.setLecturer(lecturer);
            mentoringSession.setStatus(SessionStatus.PENDING);

            mentoringSessionService.save(mentoringSession);

            redirectAttributes.addFlashAttribute("success", "Đặt lịch tư vấn thành công!");
            return "redirect:/client/booking";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi đặt lịch. Vui lòng thử lại!");
            model.addAttribute("bookingDTO", bookingDTO);
            model.addAttribute("departments", departmentService.getAlls());
            if (bookingDTO.getDepartmentId() != null) {
                model.addAttribute("lecturers", lecturerService.findByDepartmentId(bookingDTO.getDepartmentId()));
            }
            return "client/booking";
        }
    }

    @GetMapping("/history")
    public String history(Model model, HttpSession session , @RequestParam(defaultValue = "0") int page) {
        String username = (String) session.getAttribute("userLogin");
        if (username == null) return "redirect:/auth/login";

        String role = (String) session.getAttribute("role");
        if ("admin".equals(role)) {
            return "redirect:/admin/equipments";
        } else if ("lecturer".equals(role)) {
            return "redirect:/lecturer/dashboard";
        }

        User userCurrent = userService.findByUsername(username);
        UserProfile userProfile = userProfileService.findByUserId(userCurrent.getId());

        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(userProfile != null ? userProfile.getFullName() : "Người dùng");
        userDTO.setUsername(username);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("userImage", userProfile != null ? userProfile.getImageUrl() : "");

        int size =5;

        Pageable pageable = PageRequest.of(page, size, Sort.by("sessionDate").descending());

        Page<MentoringSession> mentoringSessionPage = mentoringSessionService.getPaginatedSessions(userCurrent.getId(), pageable);

        model.addAttribute("mentoringSessions", mentoringSessionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", mentoringSessionPage.getTotalPages());

        return "client/history_booking";
    }

    @PostMapping("/cancel")
    public String cancel(RedirectAttributes redirectAttributes, HttpSession session , @RequestParam(required = false) Long msId) {
        MentoringSession mentoringSession = mentoringSessionService.getSessionById(msId);
        if (mentoringSession != null) {
            LocalDateTime sessionStart = LocalDateTime.of(mentoringSession.getSessionDate(), mentoringSession.getStartTime());
            if (LocalDateTime.now().plusHours(24).isBefore(sessionStart)){
                mentoringSession.setStatus(SessionStatus.CANCELLED);
                mentoringSessionService.save(mentoringSession);
                redirectAttributes.addFlashAttribute("toastMessage", "Hủy lịch thành công!");
                redirectAttributes.addFlashAttribute("toastType", "success");
            } else {
                redirectAttributes.addFlashAttribute("toastMessage", "Không thể hủy lịch trong vòng 24h trước khi diễn ra!");
                redirectAttributes.addFlashAttribute("toastType", "error");
            }
        }
        return "redirect:/client/history";
    }

    @GetMapping("/dashboardClient")
    public String dashboardClient(Model model, HttpSession session ) {
        String username = (String) session.getAttribute("userLogin");
        if (username == null) return "redirect:/auth/login";

        String role = (String) session.getAttribute("role");
        if ("admin".equals(role)) {
            return "redirect:/admin/equipments";
        } else if ("lecturer".equals(role)) {
            return "redirect:/lecturer/dashboard";
        }

        User userCurrent = userService.findByUsername(username);
        UserProfile userProfile = userProfileService.findByUserId(userCurrent.getId());

        UserDTO userDTO = new UserDTO();
        userDTO.setFullName(userProfile != null ? userProfile.getFullName() : "Người dùng");
        userDTO.setUsername(username);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("userImage", userProfile != null ? userProfile.getImageUrl() : "");
        return "client/dashboard_client";
    }
}


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
import ra.model.entity.Equipment;
import ra.service.EquipmentService;

import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/equipments")
    public String equipments(@RequestParam(defaultValue = "1") Integer page, Model model , HttpSession session) {
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
        int size = 1;
        Page<Equipment> equipmentPage = equipmentService.findAll(page, size);

        model.addAttribute("userLogin", user.toString());
        model.addAttribute("equipments", equipmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", equipmentPage.getTotalPages());

        if (!model.containsAttribute("equipment")) {
            model.addAttribute("equipment", new EquipmentDTO());
        }

        return "admin/equipments";
    }

    @PostMapping("/add")
    public String addEquipment(@Valid @ModelAttribute("equipment") EquipmentDTO equipmentDTO , BindingResult result, RedirectAttributes redirectAttributes, HttpSession session , @RequestParam(defaultValue = "1") Integer page) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.equipment", result);
            redirectAttributes.addFlashAttribute("equipment", equipmentDTO);
            redirectAttributes.addFlashAttribute("openModal", true);
            return "redirect:/admin/equipments?page=" + page;
        }
        Equipment equipment = new Equipment();
        equipment.setName(equipmentDTO.getName());
        equipment.setQuantity(equipmentDTO.getQuantity());
        equipment.setCategoryName(equipmentDTO.getCategoryName());
        equipment.setAvailable(equipmentDTO.getQuantity());
        equipment.setDelete(false);
        equipmentService.save(equipment);
        redirectAttributes.addFlashAttribute("success", "Thêm thành công thiết bị");
        return "redirect:/admin/equipments?page=" + page;
    }

    @GetMapping("/delete-confirm/{id}")
    public String showDeleteConfirm(@PathVariable("id") Long id, @RequestParam(defaultValue = "1") Integer page, Model model, HttpSession session) {
        int size = 1;
        Page<Equipment> equipmentPage = equipmentService.findAll(page, size);

        Equipment equipment = equipmentService.findById(id);

        model.addAttribute("equipments", equipmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", equipmentPage.getTotalPages());
        model.addAttribute("userLogin", session.getAttribute("userLogin"));

        model.addAttribute("deleteId", id);
        model.addAttribute("openDeleteModal", true);
        model.addAttribute("equipment", new EquipmentDTO());

        model.addAttribute("deleteName", equipment.getName());

        return "admin/equipments";
    }

    @GetMapping("/delete/{id}")
    public String deleteEquipment(@PathVariable("id") Long id, RedirectAttributes redirectAttributes , HttpSession session) {
        Equipment equipment = equipmentService.findById(id);
        equipment.setDelete(true);
        equipmentService.save(equipment);
        redirectAttributes.addFlashAttribute("success", "Xóa thiết bị thành công!");
        return "redirect:/admin/equipments";
    }

    @GetMapping("/update/{id}")
    public String updateEquipment(@PathVariable("id") Long id, @RequestParam(defaultValue = "1") Integer page, Model model, HttpSession session) {
        int size = 1;
        Page<Equipment> equipmentPage = equipmentService.findAll(page, size);

        Equipment equipment = equipmentService.findById(id);

        model.addAttribute("equipments", equipmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", equipmentPage.getTotalPages());
        model.addAttribute("userLogin", session.getAttribute("userLogin"));

        model.addAttribute("equipment", equipment);
        model.addAttribute("openEditModal", true);

        return "admin/equipments";
    }

    @PostMapping("/update")
    public String updateEquiment(@Valid @ModelAttribute("equipment") EquipmentDTO equipmentDTO , BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.equipment", result);
            redirectAttributes.addFlashAttribute("equipment", equipmentDTO);
            redirectAttributes.addFlashAttribute("openModal", true);
            return "redirect:/admin/equipments";
        }
        Equipment equipment = equipmentService.findById(equipmentDTO.getId());

        if (equipment != null) {
            equipment.setName(equipmentDTO.getName());
            equipment.setQuantity(equipmentDTO.getQuantity());
            equipment.setCategoryName(equipmentDTO.getCategoryName());
            equipmentService.save(equipment);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Thiết bị không tồn tại hoặc đã bị xóa!");
        }
        return "redirect:/admin/equipments";
    }
}

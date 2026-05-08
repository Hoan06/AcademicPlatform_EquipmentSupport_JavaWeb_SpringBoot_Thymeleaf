//package ra.validate.impl;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import ra.model.dto.EquipmentDTO;
//import ra.validate.ValidEquipment;
//
//public class EquipmentValidator implements ConstraintValidator<ValidEquipment, EquipmentDTO> {
//
//    @Override
//    public boolean isValid(EquipmentDTO dto, ConstraintValidatorContext context) {
//        if (dto.getQuantity() == null || dto.getAvailable() == null) {
//            return true;
//        }
//        return dto.getAvailable() <= dto.getQuantity();
//    }
//}
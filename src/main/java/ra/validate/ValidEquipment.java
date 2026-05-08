//package ra.validate;
//
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//import ra.validate.impl.EquipmentValidator;
//
//import java.lang.annotation.*;
//
//@Constraint(validatedBy = EquipmentValidator.class)
//@Target({ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ValidEquipment {
//    String message() default "Số lượng khả dụng không được lớn hơn tổng số lượng";
//    Class<?>[] groups() default {};
//    Class<? extends Payload>[] payload() default {};
//}
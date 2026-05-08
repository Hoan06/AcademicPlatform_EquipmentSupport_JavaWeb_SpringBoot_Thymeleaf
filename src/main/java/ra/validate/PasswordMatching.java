package ra.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ra.validate.impl.PasswordMatchingValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = PasswordMatchingValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatching {
    String message() default "Mật khẩu xác nhận không khớp";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
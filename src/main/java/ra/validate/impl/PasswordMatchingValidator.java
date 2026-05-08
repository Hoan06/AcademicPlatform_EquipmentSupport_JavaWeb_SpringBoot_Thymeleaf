package ra.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ra.model.dto.RegisterDTO;
import ra.validate.PasswordMatching;

public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, RegisterDTO> {

    @Override
    public boolean isValid(RegisterDTO dto, ConstraintValidatorContext context) {
        if (dto.getPassword() == null || !dto.getPassword().equals(dto.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
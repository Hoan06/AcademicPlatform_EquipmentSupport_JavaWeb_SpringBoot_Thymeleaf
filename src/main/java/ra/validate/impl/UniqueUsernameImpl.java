package ra.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.repository.auth.UserRepository;
import ra.validate.UniqueUsername;

@Component
public class UniqueUsernameImpl implements ConstraintValidator<UniqueUsername, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (userRepository.findByUsername(value) == null) {
            return true;
        }
        return false;
    }
}

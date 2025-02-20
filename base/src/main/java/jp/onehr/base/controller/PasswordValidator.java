package jp.onehr.base.controller;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordMatch,ValidDto> {
    @Override
    public boolean isValid(ValidDto user, ConstraintValidatorContext context) {
        if (user.getPassword() == null || user.getConfirmPassword() == null) {
            return true; // 让其他字段的校验来处理 null 值
        }
        boolean isValid = user.getPassword().equals(user.getConfirmPassword());
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords must match").addConstraintViolation();
        }
        return isValid;
    }
}

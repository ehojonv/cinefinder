package br.com.fiap.cinefinder.validation;


import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
    private final Pattern PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null) {
            return false;
        }
        return PATTERN.matcher(password).matches();

    }

}

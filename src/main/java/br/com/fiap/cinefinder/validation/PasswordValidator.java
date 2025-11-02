package br.com.fiap.cinefinder.validation;

import java.util.regex.Pattern;

import br.com.fiap.cinefinder.expection.InvalidPasswordException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        // pelo menos: 8 caracteres - 1 maiuscula - 1 minuscula - 1 numero - 1 caractere
        // especial
        final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        var isValid = password != null
                && Pattern.matches(PASSWORD_REGEX, password);

        if (!isValid) {
            throw new InvalidPasswordException("Password does not meet the required criteria.");
        }

        return true;
    }

}

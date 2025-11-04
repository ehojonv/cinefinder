package br.com.fiap.cinefinder.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

	String message() default "{password.invalid.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}

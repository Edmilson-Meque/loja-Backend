package com.ecommerce.ecommerce.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
        message = "Password must contain at least 8 characters, one uppercase, " +
                "one lowercase, one number and one special character")
public @interface StrongPassword {
    String message() default "Password is not strong enough";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
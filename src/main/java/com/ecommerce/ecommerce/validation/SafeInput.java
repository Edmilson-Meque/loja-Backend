package com.ecommerce.ecommerce.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^[\\p{L}\\p{M}\\p{N}\\p{P}\\p{Zs}]*$",
        message = "Contém caracteres inválidos")
public @interface SafeInput {
    String message() default "Input contains invalid characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

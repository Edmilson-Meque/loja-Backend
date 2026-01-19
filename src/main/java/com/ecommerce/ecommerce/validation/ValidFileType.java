package com.ecommerce.ecommerce.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileType {
    String message() default "Invalid file type";
    String[] allowedTypes() default {"jpg", "jpeg", "png", "gif", "pdf"};
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

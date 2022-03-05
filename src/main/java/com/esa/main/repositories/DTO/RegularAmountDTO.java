package com.esa.main.repositories.DTO;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = RegularAmountValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegularAmountDTO {
    boolean isAmountBlank() default false;
    boolean isAmountEmpty() default false;
    boolean isFrequencyNullable() default false;
    String message() default "The received data is not a valid RegularAmount.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

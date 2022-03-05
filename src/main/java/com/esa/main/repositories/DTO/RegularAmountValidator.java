package com.esa.main.repositories.DTO;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * @Criteria
 * @1. Invalid amount
 *  - GIVEN any Frequency, WHEN a non-numeric or blank amount is provided, THEN no validation Error
 *
 * @2. Null Frequency
 *  - GIVEN any Amount, WHEN a null Frequency is provided, THEN no validation Error
 *
 * @3. Weekly
 *  - GIVEN a WEEK Frequency , WHEN any Amount is provided, THEN no validation Error
 *
 * @4. Monthly
 *  - GIVEN a MONTH Frequency, WHEN any Amount is provided, THEN no validation Error.
 *
 * @5. Validated as valid
 *  - GIVEN a Frequency is in set TWO_WEEK, FOUR_WEEK, QUARTER, YEAR, AND associated Number of Weeks is 2, 4, 13, 52
 *  - WHEN an Amount that divides the number of weeks to a whole number of pence is provided
 *  - THEN no validation error
 *
 * @6. Validated as Invalid
 *  - GIVEN a Frequency is in set TWO_WEEK, FOUR_WEEK, QUARTER, YEAR, AND associated Number of Weeks is 2, 4, 13, 52
 *  - WHEN an Amount that does NOT divide the number of weeks to a whole number of pence is provided
 *  - THEN throw a validation error
 */

@Slf4j
public class RegularAmountValidator implements ConstraintValidator<RegularAmountDTO, RegularAmount> {
    private boolean isAmountBlank;
    private boolean isAmountEmpty;
    private boolean isFrequencyNullable;

    @Override
    public void initialize(RegularAmountDTO regularAmountDTO) {
        ConstraintValidator.super.initialize(regularAmountDTO);
        this.isAmountBlank = regularAmountDTO.isAmountBlank();
        this.isAmountEmpty = regularAmountDTO.isAmountEmpty();
        this.isFrequencyNullable = regularAmountDTO.isFrequencyNullable();
    }

    @Override
    public boolean isValid(RegularAmount regularAmount, ConstraintValidatorContext context) {

        boolean isBlank = isAmountBlank && regularAmount.getAmount().isBlank();
        boolean isEmpty = isAmountEmpty && regularAmount.getAmount().isEmpty();
        boolean isAmountNull = regularAmount.getAmount() != null;

        if (isBlank || isEmpty) {
            regularAmount.setAmount("0");
        }

        // Frequency Validation boolean.
        boolean validFrequency = isFrequencyNullable && Arrays.stream(RegularAmount.Frequency.values())
                .filter(f -> {
                    if(regularAmount.getFrequency() != null) {
                        return f.name().equals(regularAmount.getFrequency().name());
                    }
                    return false;
                }).
                toList()
                .size() == 1;

        if(regularAmount.getAmount().contains("-")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Amount given is invalid. Value should be a valid whole number. Eg: amount should be greater than 0")
                    .addConstraintViolation();
            return false;
        }


        // Filtering the Frequency Enums down to multiple of weeks (week count is > 1: Excludes WEEK and MONTH).
        List<RegularAmount.Frequency> frequencyMultiplesOfWeeks =
                Arrays
                        .stream(RegularAmount.Frequency.values())
                        .filter(f -> f.name().equals(RegularAmount.Frequency.TWO_WEEK.name())
                                || f.name().equals(RegularAmount.Frequency.FOUR_WEEK.name())
                                || f.name().equals(RegularAmount.Frequency.QUARTER.name())
                                || f.name().equals(RegularAmount.Frequency.YEAR.name()))
                        .toList();

        // If Frequency is a valid frequency enum value or null
        if (validFrequency || regularAmount.getFrequency() == null) {

            // Checking regular amount's value
            if(isAmountNull || isBlank || isEmpty || regularAmount.getAmount().length() > 0) {

                // If frequencyMultiplesOfWeeks's collection size is not empty.
               if(frequencyMultiplesOfWeeks.size() > 0 && frequencyMultiplesOfWeeks.contains(regularAmount.getFrequency())) {

                   // Now to calculate the real validation.
                   int amountToPence;
                   int weeksFromFrequency = Integer.parseInt(regularAmount.getFrequency().getFrequency());


                   if(regularAmount.getAmount().contains(".")) {
                       // Grab amount, parse it to float
                       amountToPence = Integer.parseInt(String.valueOf(Float.parseFloat(regularAmount.getAmount()) * 10));
                   } else {
                       amountToPence = Integer.parseInt(regularAmount.getAmount());
                   }

                   var weeklyAmount = amountToPence / weeksFromFrequency;

                   if(((Object) weeklyAmount).getClass().getSimpleName().equals("Integer") && weeklyAmount > 0) {
                        return true;
                   }
                   context.disableDefaultConstraintViolation();
                   context.buildConstraintViolationWithTemplate(String.format("The Amount provided: %s cannot be divided equally for the Frequency: %s weeks provided. Please provided a valid number", regularAmount.getAmount(), regularAmount.getFrequency().getFrequency() ))
                           .addConstraintViolation();
                   return false;

               }
               return true;
            }

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Amount entered must satisfy the following conditions, isAmountNull: " + isAmountNull
                            + ", isBlank: " + isBlank
                            + ", isEmpty: " + isEmpty
                            + ", and length of amount should be greater than 0: " + regularAmount.getAmount().length() + "." )
                    .addConstraintViolation();

            return false;

        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Frequency given is invalid!")
                .addConstraintViolation();

        return false;
    }
}

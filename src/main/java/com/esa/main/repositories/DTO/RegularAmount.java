package com.esa.main.repositories.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@RegularAmountDTO(isAmountBlank = true, isAmountEmpty = true, isFrequencyNullable = true)
public class RegularAmount {

    private Frequency frequency;
    private String amount;


    public enum Frequency {
        WEEK("1"), TWO_WEEK("2"), FOUR_WEEK("4"), MONTH("MONTH"), QUARTER("13"), YEAR("52"), NULL(null);

        private final String frequency;

        Frequency(String frequency) {
            this.frequency = frequency;
        }

        public String getFrequency() {
            return frequency;
        }

    }
}

package com.esa.main.services;


import com.esa.main.repositories.DTO.RegularAmount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EsaService {

    public void registerCitizenESA(RegularAmount regularAmount) {

        String amount = regularAmount.getAmount();
        RegularAmount.Frequency frequency = regularAmount.getFrequency();
        String isFrequencyNull = regularAmount.getFrequency() != null ? regularAmount.getFrequency().getFrequency() : "value of frequency is null";
        log.info("Regular Amount: amount is {}, {} and {}", amount, frequency, isFrequencyNull);

    }

}

package com.esa.main.controllers;

import com.esa.main.exceptions.ApiRestException;
import com.esa.main.services.EsaService;
import com.esa.main.repositories.DTO.RegularAmount;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@Validated
@Controller
@Slf4j
@RequestMapping(path = "api/v1/esa")
public class EsaController {
    private final EsaService esaService;

    @Autowired
    public EsaController(EsaService esaService) {
        this.esaService = esaService;
    }

    @PostMapping
    public ResponseEntity<Object> registerCitizenESA(@Valid @RequestBody RegularAmount regularAmount, BindingResult result) throws ApiRestException, MethodArgumentNotValidException {

        JSONObject response = new JSONObject();

        response.put("message", "Data validation successful, and has been saved.");
        response.put("timestamp", ZonedDateTime.now(ZoneId.of("Z")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("status", HttpStatus.OK);

        esaService.registerCitizenESA(regularAmount);
        return new ResponseEntity<Object>(response, HttpStatus.OK);

    }

}

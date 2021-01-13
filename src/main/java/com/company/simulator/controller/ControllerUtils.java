package com.company.simulator.controller;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ControllerUtils {
    static Map<String, String> getErrors(final BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
           .stream().collect(
                Collectors.toMap(
                    fieldError -> String.format("%sError", fieldError.getField()),
                    FieldError::getDefaultMessage
                )
            );
    }
}

package com.spring.citronix.web.vm.response.field;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Setter
@Getter
public class FieldResponseVM {
    private UUID fieldId;
    private double fieldArea;
    private UUID farmId;
    private String farmName;

}

package com.quantity.measurement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class QuantityInputDTO {

    @Valid
    @NotNull(message = "First quantity is required")
    private QuantityDTO thisQuantityDTO;

    @Valid
    private QuantityDTO thatQuantityDTO;

    private String targetUnit;

    public QuantityInputDTO() {
    }

    public QuantityDTO getThisQuantityDTO() {
        return thisQuantityDTO;
    }

    public void setThisQuantityDTO(QuantityDTO thisQuantityDTO) {
        this.thisQuantityDTO = thisQuantityDTO;
    }

    public QuantityDTO getThatQuantityDTO() {
        return thatQuantityDTO;
    }

    public void setThatQuantityDTO(QuantityDTO thatQuantityDTO) {
        this.thatQuantityDTO = thatQuantityDTO;
    }

    public String getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }
}
package com.quantity.measurement.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class QuantityDTO {

    @NotNull(message = "Value is required")
    private Double value;

    @NotEmpty(message = "Unit is required")
    private String unit;

    @NotEmpty(message = "Measurement type is required")
    @Pattern(
            regexp = "LENGTH|WEIGHT|VOLUME|TEMPERATURE|LengthUnit|WeightUnit|VolumeUnit|TemperatureUnit",
            message = "Measurement type must be valid"
    )
    private String measurementType;

    private boolean error;

    private String errorMessage;

    public QuantityDTO() {
    }

    public QuantityDTO(double value, String unit, String measurementType) {
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }

    public QuantityDTO(boolean error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
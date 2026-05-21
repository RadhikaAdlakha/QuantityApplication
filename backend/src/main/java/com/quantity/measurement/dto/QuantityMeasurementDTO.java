package com.quantity.measurement.dto;

import com.quantity.measurement.entity.QuantityMeasurementEntity;

import java.time.LocalDateTime;
import java.util.List;

public class QuantityMeasurementDTO {

    private Long id;
    private String operationType;
    private String measurementType;
    private Double operand1Value;
    private String operand1Unit;
    private Double operand2Value;
    private String operand2Unit;
    private Double resultValue;
    private String resultUnit;
    private boolean error;
    private String errorMessage;
    private LocalDateTime createdAt;

    public QuantityMeasurementDTO() {
    }

    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity quantityMeasurementEntity) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setId(quantityMeasurementEntity.getId());
        dto.setOperationType(quantityMeasurementEntity.getOperationType());
        dto.setMeasurementType(quantityMeasurementEntity.getMeasurementType());
        dto.setOperand1Value(quantityMeasurementEntity.getOperand1Value());
        dto.setOperand1Unit(quantityMeasurementEntity.getOperand1Unit());
        dto.setOperand2Value(quantityMeasurementEntity.getOperand2Value());
        dto.setOperand2Unit(quantityMeasurementEntity.getOperand2Unit());
        dto.setResultValue(quantityMeasurementEntity.getResultValue());
        dto.setResultUnit(quantityMeasurementEntity.getResultUnit());
        dto.setError(quantityMeasurementEntity.isError());
        dto.setErrorMessage(quantityMeasurementEntity.getErrorMessage());
        dto.setCreatedAt(quantityMeasurementEntity.getCreatedAt());
        return dto;
    }

    public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> quantityMeasurementEntities) {
        return quantityMeasurementEntities.stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .toList();
    }

    public QuantityMeasurementEntity toEntity() {
        QuantityMeasurementEntity quantityMeasurementEntity = new QuantityMeasurementEntity();
        quantityMeasurementEntity.setId(id);
        quantityMeasurementEntity.setOperationType(operationType);
        quantityMeasurementEntity.setMeasurementType(measurementType);
        quantityMeasurementEntity.setOperand1Value(operand1Value == null ? 0.0 : operand1Value);
        quantityMeasurementEntity.setOperand1Unit(operand1Unit);
        quantityMeasurementEntity.setOperand2Value(operand2Value == null ? 0.0 : operand2Value);
        quantityMeasurementEntity.setOperand2Unit(operand2Unit);
        quantityMeasurementEntity.setResultValue(resultValue == null ? 0.0 : resultValue);
        quantityMeasurementEntity.setResultUnit(resultUnit);
        quantityMeasurementEntity.setError(error);
        quantityMeasurementEntity.setErrorMessage(errorMessage);
        quantityMeasurementEntity.setCreatedAt(createdAt);
        return quantityMeasurementEntity;
    }

    public static List<QuantityMeasurementEntity> toEntityList(List<QuantityMeasurementDTO> dtos) {
        return dtos.stream()
                .map(QuantityMeasurementDTO::toEntity)
                .toList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public Double getOperand1Value() {
        return operand1Value;
    }

    public void setOperand1Value(Double operand1Value) {
        this.operand1Value = operand1Value;
    }

    public String getOperand1Unit() {
        return operand1Unit;
    }

    public void setOperand1Unit(String operand1Unit) {
        this.operand1Unit = operand1Unit;
    }

    public Double getOperand2Value() {
        return operand2Value;
    }

    public void setOperand2Value(Double operand2Value) {
        this.operand2Value = operand2Value;
    }

    public String getOperand2Unit() {
        return operand2Unit;
    }

    public void setOperand2Unit(String operand2Unit) {
        this.operand2Unit = operand2Unit;
    }

    public Double getResultValue() {
        return resultValue;
    }

    public void setResultValue(Double resultValue) {
        this.resultValue = resultValue;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public void setResultUnit(String resultUnit) {
        this.resultUnit = resultUnit;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
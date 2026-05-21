package com.quantity.measurement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@jakarta.persistence.Entity
@Table(name = "quantity_measurement_entity")
public class QuantityMeasurementEntity
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operand1_value")
    private double operand1Value;

    @Column(name = "operand1_unit", length = 50)
    private String operand1Unit;

    @Column(name = "operand2_value")
    private double operand2Value;

    @Column(name = "operand2_unit", length = 50)
    private String operand2Unit;

    @Column(name = "measurement_type", length = 50)
    private String measurementType;

    @Column(name = "operation_type", length = 50)
    private String operationType;

    @Column(name = "result_value")
    private double resultValue;

    @Column(name = "result_unit", length = 50)
    private String resultUnit;

    @Column(name = "is_error")
    private boolean error;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public QuantityMeasurementEntity() {
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getOperand1Value() {
        return operand1Value;
    }

    public void setOperand1Value(double operand1Value) {
        this.operand1Value = operand1Value;
    }

    public String getOperand1Unit() {
        return operand1Unit;
    }

    public void setOperand1Unit(String operand1Unit) {
        this.operand1Unit = operand1Unit;
    }

    public double getOperand2Value() {
        return operand2Value;
    }

    public void setOperand2Value(double operand2Value) {
        this.operand2Value = operand2Value;
    }

    public String getOperand2Unit() {
        return operand2Unit;
    }

    public void setOperand2Unit(String operand2Unit) {
        this.operand2Unit = operand2Unit;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public double getResultValue() {
        return resultValue;
    }

    public void setResultValue(double resultValue) {
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
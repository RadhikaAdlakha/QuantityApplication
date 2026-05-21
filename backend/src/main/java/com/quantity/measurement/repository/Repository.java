package com.quantity.measurement.repository;

import java.util.List;

import com.quantity.measurement.entity.QuantityMeasurementEntity;

public interface Repository{

    // Save measurement
    void save(QuantityMeasurementEntity entity);

    // Get all measurements
    List<QuantityMeasurementEntity> getAllMeasurements();

    // Get by operation type
    List<QuantityMeasurementEntity>
    getMeasurementsByOperation(String operationType);

    // Get by measurement type
    List<QuantityMeasurementEntity>
    getMeasurementsByType(String measurementType);

    // Delete all records
    void deleteAll();

    // Get total count
    long getTotalCount();
}
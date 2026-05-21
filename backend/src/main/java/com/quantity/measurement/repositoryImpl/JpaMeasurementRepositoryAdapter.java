package com.quantity.measurement.repositoryImpl;

import com.quantity.measurement.entity.QuantityMeasurementEntity;
import com.quantity.measurement.repository.QuantityMeasurementRepository;
import com.quantity.measurement.repository.Repository;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Primary
@org.springframework.stereotype.Repository
public class JpaMeasurementRepositoryAdapter implements Repository {

    private final QuantityMeasurementRepository repository;

    public JpaMeasurementRepositoryAdapter(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(QuantityMeasurementEntity quantityMeasurementEntity) {
        repository.save(quantityMeasurementEntity);
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return repository.findAll();
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType) {
        return repository.findByOperationType(operationType);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        return repository.findByMeasurementType(measurementType);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public long getTotalCount() {
        return repository.count();
    }

    public List<QuantityMeasurementEntity> getErroredMeasurements() {
        return repository.findByErrorTrue();
    }

    public long countSuccessfulByOperation(String operationType) {
        return repository.countByOperationTypeAndErrorFalse(operationType);
    }
}
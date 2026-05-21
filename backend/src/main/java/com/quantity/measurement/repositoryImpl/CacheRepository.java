package com.quantity.measurement.repositoryImpl;

import com.quantity.measurement.entity.QuantityMeasurementEntity;
import com.quantity.measurement.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class CacheRepository implements Repository {

    private static CacheRepository instance;

    private final List<QuantityMeasurementEntity> cache = new ArrayList<>();

    //constructor
    private CacheRepository() {
    }

    public static CacheRepository getInstance() {
        if (instance == null) {
            instance = new CacheRepository();
        }
        return instance;
    }

    @Override
    public void save(QuantityMeasurementEntity quantityMeasurementEntity) {
        cache.add(quantityMeasurementEntity);
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return List.of();
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType) {
        return List.of();
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        return List.of();
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public long getTotalCount() {
        return 0;
    }
}
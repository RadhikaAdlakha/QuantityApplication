package com.quantity.measurement.repository;

import com.quantity.measurement.entity.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    List<QuantityMeasurementEntity> findByOperationType(String operationType);

    List<QuantityMeasurementEntity> findByMeasurementType(String measurementType);

    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

    @Query("""
            select e
            from QuantityMeasurementEntity e
            where e.operationType = :operationType
            and e.error = false
            """)
    List<QuantityMeasurementEntity> findSuccessfulByOperationType(String operationType);

    long countByOperationTypeAndErrorFalse(String operationType);

    List<QuantityMeasurementEntity> findByErrorTrue();
}
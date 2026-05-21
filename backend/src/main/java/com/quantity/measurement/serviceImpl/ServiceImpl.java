package com.quantity.measurement.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.quantity.measurement.dto.QuantityDTO;
import com.quantity.measurement.entity.QuantityMeasurementEntity;
import com.quantity.measurement.enumImpl.LengthUnit;
import com.quantity.measurement.enumImpl.VolumeUnit;
import com.quantity.measurement.enumImpl.WeightUnit;
import com.quantity.measurement.enumImpl.TemperatureUnit;
import com.quantity.measurement.enums.IMeasurable;
import com.quantity.measurement.exception.Exception;
import com.quantity.measurement.model.Quantity;
import com.quantity.measurement.repository.Repository;
import com.quantity.measurement.service.Service;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    private static final Logger logger = LoggerFactory.getLogger(ServiceImpl.class);

    private final Repository repository;

    // DI using Constructor
    public ServiceImpl(Repository repository2) {
        this.repository = repository2;

        logger.info("QuantityMeasurementService initialized");

    }

    private IMeasurable getUnit(String unit, String type) {

        String normalizedUnit = normalizeUnit(unit);
        String normalizedType = normalizeMeasurementType(type);

        logger.debug("Resolving unit {} for type {}", normalizedUnit, normalizedType);

        return switch (normalizedType) {
            case "LENGTH" -> LengthUnit.valueOf(normalizedUnit);
            case "WEIGHT" -> WeightUnit.valueOf(normalizedUnit);
            case "VOLUME" -> VolumeUnit.valueOf(normalizedUnit);
            case "TEMPERATURE" -> TemperatureUnit.valueOf(normalizedUnit);
            default -> throw new Exception("Invalid type");
        };
    }

    private String normalizeMeasurementType(String type) {
        if (type == null || type.isBlank()) {
            throw new Exception("Measurement type is required");
        }

        return switch (type.trim().toUpperCase()) {
            case "LENGTH", "LENGTHUNIT" -> "LENGTH";
            case "WEIGHT", "WEIGHTUNIT" -> "WEIGHT";
            case "VOLUME", "VOLUMEUNIT" -> "VOLUME";
            case "TEMPERATURE", "TEMPERATUREUNIT" -> "TEMPERATURE";
            default -> throw new Exception("Invalid type");
        };
    }

    private String normalizeUnit(String unit) {
        if (unit == null || unit.isBlank()) {
            throw new Exception("Unit is required");
        }

        String normalized = unit.trim().toUpperCase();
        return switch (normalized) {
            case "INCHES" -> "INCH";
            case "YARD" -> "YARDS";
            case "CENTIMETER" -> "CENTIMETERS";
            case "LITER" -> "LITRE";
            case "MILLILITER" -> "MILLILITRE";
            default -> normalized;
        };
    }

    private QuantityMeasurementEntity createEntity(
            QuantityDTO q1,
            QuantityDTO q2,
            String measurementType,
            String operationType,
            double resultValue,
            String resultUnit) {
        QuantityMeasurementEntity quantityMeasurementEntity = new QuantityMeasurementEntity();
        quantityMeasurementEntity.setOperand1Value(q1.getValue());
        quantityMeasurementEntity.setOperand1Unit(q1.getUnit());
        if (q2 != null) {
            quantityMeasurementEntity.setOperand2Value(q2.getValue());
            quantityMeasurementEntity.setOperand2Unit(q2.getUnit());
        }
        quantityMeasurementEntity.setMeasurementType(measurementType);
        quantityMeasurementEntity.setOperationType(operationType);
        quantityMeasurementEntity.setResultValue(resultValue);
        quantityMeasurementEntity.setResultUnit(resultUnit);
        return quantityMeasurementEntity;
    }

    private void saveError(QuantityDTO q1, QuantityDTO q2, String operationType, java.lang.Exception exception) {
        try {
            QuantityMeasurementEntity quantityMeasurementEntity = new QuantityMeasurementEntity();
            if (q1 != null) {
            	quantityMeasurementEntity.setOperand1Value(q1.getValue());
                quantityMeasurementEntity.setOperand1Unit(q1.getUnit());
                quantityMeasurementEntity.setMeasurementType(q1.getMeasurementType());
            }
            if (q2 != null) {
            	quantityMeasurementEntity.setOperand2Value(q2.getValue());
                quantityMeasurementEntity.setOperand2Unit(q2.getUnit());
            }
            quantityMeasurementEntity.setOperationType(operationType);
            quantityMeasurementEntity.setResultUnit("ERROR");
            quantityMeasurementEntity.setError(true);
            quantityMeasurementEntity.setErrorMessage(exception.getMessage());
            repository.save(quantityMeasurementEntity);
        } catch (java.lang.Exception repositoryException) {
            logger.warn("Unable to persist failed {} operation", operationType, repositoryException);
        }
    }

    @Override
    @Transactional
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2, String targetUnit) {

        logger.info("ADD operation started");

        try {

            IMeasurable u1 = getUnit(q1.getUnit(), q1.getMeasurementType());

            IMeasurable u2 = getUnit(q2.getUnit(), q2.getMeasurementType());

            Quantity<?> result = new Quantity<>(q1.getValue(), u1).add(new Quantity<>(q2.getValue(), u2),
                    getUnit(targetUnit, q1.getMeasurementType()));

            String measurementType = normalizeMeasurementType(q1.getMeasurementType());

            repository.save(createEntity(q1, q2, measurementType, "ADD", result.getValue(), targetUnit));

            logger.info("ADD operation successful");

            return new QuantityDTO(result.getValue(), targetUnit, q1.getMeasurementType());

        } catch (java.lang.Exception e) {

            logger.error("ADD operation failed", e);

            saveError(q1, q2, "ADD", e);

            return new QuantityDTO(true, e.getMessage());
        }
    }

    @Override
    @Transactional
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2, String targetUnit) {

        logger.info("SUBTRACT operation started");

        try {

            IMeasurable u1 = getUnit(q1.getUnit(), q1.getMeasurementType());

            IMeasurable u2 = getUnit(q2.getUnit(), q2.getMeasurementType());

            Quantity<?> result = new Quantity<>(q1.getValue(), u1)
                    .subtract(
                            new Quantity<>(q2.getValue(), u2),
                            getUnit(targetUnit, q1.getMeasurementType()));

            String measurementType = normalizeMeasurementType(q1.getMeasurementType());

            repository.save(createEntity(q1, q2, measurementType, "SUBTRACT", result.getValue(), targetUnit));

            logger.info("SUBTRACT operation successful");

            return new QuantityDTO(result.getValue(), targetUnit, q1.getMeasurementType());

        } catch (java.lang.Exception e) {

            logger.error("SUBTRACT operation failed", e);

            saveError(q1, q2, "SUBTRACT", e);

            return new QuantityDTO(true, e.getMessage());
        }
    }

    @Override
    @Transactional
    public QuantityDTO divide(QuantityDTO q1, QuantityDTO q2) {

        logger.info("DIVIDE operation started");

        try {

            IMeasurable u1 = getUnit(q1.getUnit(), q1.getMeasurementType());

            IMeasurable u2 = getUnit(q2.getUnit(), q2.getMeasurementType());

            double result = new Quantity<>(q1.getValue(), u1)
                    .divide(new Quantity<>(q2.getValue(), u2));

            String measurementType = normalizeMeasurementType(q1.getMeasurementType());

            repository.save(createEntity(q1, q2, measurementType, "DIVIDE", result, "SCALAR"));

            logger.info("DIVIDE operation successful");

            return new QuantityDTO(result, "SCALAR", q1.getMeasurementType());

        } catch (java.lang.Exception e) {

            logger.error("DIVIDE operation failed", e);

            saveError(q1, q2, "DIVIDE", e);

            return new QuantityDTO(true, e.getMessage());
        }
    }

    @Override
    @Transactional
    public QuantityDTO convert(QuantityDTO q, String targetUnit) {

        logger.info("CONVERT operation started");

        try {
            IMeasurable u = getUnit(q.getUnit(), q.getMeasurementType());

            Quantity<?> result = new Quantity<>(q.getValue(), u)
                    .toConvert(getUnit(targetUnit, q.getMeasurementType()));

            String measurementType = normalizeMeasurementType(q.getMeasurementType());

            repository.save(createEntity(q, null, measurementType, "CONVERT", result.getValue(), targetUnit));

            logger.info("CONVERT operation successful");

            return new QuantityDTO(result.getValue(), targetUnit, q.getMeasurementType());

        } catch (java.lang.Exception e) {

            logger.error("CONVERT operation failed : {}", e.getMessage());

            saveError(q, null, "CONVERT", e);

            return new QuantityDTO(true, e.getMessage());
        }
    }

    @Override
    @Transactional
    public QuantityDTO compare(QuantityDTO q1, QuantityDTO q2) {

        logger.info("COMPARE operation started");

        try {
            IMeasurable u1 = getUnit(q1.getUnit(), q1.getMeasurementType());
            IMeasurable u2 = getUnit(q2.getUnit(), q2.getMeasurementType());

            if (!u1.getClass().equals(u2.getClass())) {
                throw new IllegalArgumentException("Different measurement types");
            }

            boolean result = new Quantity<>(q1.getValue(), u1)
                    .equals(new Quantity<>(q2.getValue(), u2));

            String measurementType = normalizeMeasurementType(q1.getMeasurementType());

            repository.save(createEntity(q1, q2, measurementType, "COMPARE", result ? 1 : 0, "BOOLEAN"));

            logger.info("COMPARE operation successful");

            return new QuantityDTO(result ? 1 : 0, "BOOLEAN", q1.getMeasurementType());

        } catch (java.lang.Exception e) {

            logger.error("COMPARE operation failed : {}", e.getMessage());

            saveError(q1, q2, "COMPARE", e);

            return new QuantityDTO(true, e.getMessage());
        }
    }
}
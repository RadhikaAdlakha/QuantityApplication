package com.quantity.measurement.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quantity.measurement.dto.QuantityDTO;
import com.quantity.measurement.dto.QuantityInputDTO;
import com.quantity.measurement.dto.QuantityMeasurementDTO;
import com.quantity.measurement.repository.Repository;
import com.quantity.measurement.service.Service;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/quantities")
public class QuantityMeasurementController {

    private final Service service;
    private final Repository repository;

    public QuantityMeasurementController(Service service, Repository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping("/compare")
    public ResponseEntity<QuantityDTO> compare(@Valid @RequestBody QuantityInputDTO input) {
        QuantityDTO result = service.compare(input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return response(result);
    }

    @PostMapping("/convert")
    public ResponseEntity<QuantityDTO> convert(@Valid @RequestBody QuantityInputDTO input) {
        String targetUnit = input.getTargetUnit() != null
                ? input.getTargetUnit()
                : input.getThatQuantityDTO().getUnit();
        QuantityDTO result = service.convert(input.getThisQuantityDTO(), targetUnit);
        return response(result);
    }

    @PostMapping("/add")
    public ResponseEntity<QuantityDTO> add(@Valid @RequestBody QuantityInputDTO input) {
        QuantityDTO result = service.add(
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO(),
                resolveTargetUnit(input)
        );
        return response(result);
    }

    @PostMapping("/subtract")
    public ResponseEntity<QuantityDTO> subtract(@Valid @RequestBody QuantityInputDTO input) {
        QuantityDTO result = service.subtract(
                input.getThisQuantityDTO(),
                input.getThatQuantityDTO(),
                resolveTargetUnit(input)
        );
        return response(result);
    }

    @PostMapping("/divide")
    public ResponseEntity<QuantityDTO> divide(@Valid @RequestBody QuantityInputDTO input) {
        QuantityDTO result = service.divide(input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return response(result);
    }

    @GetMapping("/history")
    public List<QuantityMeasurementDTO> history() {
        return QuantityMeasurementDTO.fromEntityList(repository.getAllMeasurements());
    }

    @GetMapping("/history/operation/{operation}")
    public List<QuantityMeasurementDTO> historyByOperation(@PathVariable String operation) {
        return QuantityMeasurementDTO.fromEntityList(repository.getMeasurementsByOperation(operation));
    }

    @GetMapping("/history/type/{measurementType}")
    public List<QuantityMeasurementDTO> historyByType(@PathVariable String measurementType) {
        return QuantityMeasurementDTO.fromEntityList(repository.getMeasurementsByType(measurementType));
    }

    @GetMapping("/count/{operation}")
    public Map<String, Object> countByOperation(@PathVariable String operation) {
        long count = repository.getMeasurementsByOperation(operation).size();
        return Map.of("operation", operation, "count", count);
    }

    private ResponseEntity<QuantityDTO> response(QuantityDTO result) {
        if (result.isError()) {
            throw new com.quantity.measurement.exception.Exception(result.getErrorMessage());
        }
        return ResponseEntity.ok(result);
    }

    private String resolveTargetUnit(QuantityInputDTO input) {
        if (input.getTargetUnit() != null && !input.getTargetUnit().isBlank()) {
            return input.getTargetUnit();
        }
        return input.getThisQuantityDTO().getUnit();
    }
}
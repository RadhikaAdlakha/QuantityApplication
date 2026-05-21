CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
                                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                           operand1_value DOUBLE,
                                                           operand1_unit VARCHAR(50),
    operand2_value DOUBLE,
    operand2_unit VARCHAR(50),
    measurement_type VARCHAR(50),
    operation_type VARCHAR(50),
    result_value DOUBLE,
    result_unit VARCHAR(50),
    is_error BOOLEAN DEFAULT FALSE,
    error_message VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
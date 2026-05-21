package com.quantity.measurement.repositoryImpl;

import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quantity.measurement.entity.QuantityMeasurementEntity;
import com.quantity.measurement.exception.DatabaseException;
import com.quantity.measurement.repository.Repository;

/**
 * JDBC-based repository implementation.
 */
public class DatabaseRepository implements Repository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    DatabaseRepository.class
            );

    // H2 Database Configuration
    private static final String URL = "jdbc:h2:mem:quantitydb;DB_CLOSE_DELAY=-1";

    private static final String USERNAME = "sa";

    private static final String PASSWORD = "";

    /**
     * Constructor.
     */
    public DatabaseRepository() {

        createTableIfNotExists();
    }

    /**
     * Get database connection.
     */
    private Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                URL,
                USERNAME,
                PASSWORD
        );
    }

    /**
     * Create table if not exists.
     */
    private void createTableIfNotExists() {

        String sql = """
                CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    operand1_value DOUBLE,
                    operand1_unit VARCHAR(50),
                    operand2_value DOUBLE,
                    operand2_unit VARCHAR(50),
                    measurement_type VARCHAR(100),
                    operation_type VARCHAR(100),
                    result_value DOUBLE,
                    result_unit VARCHAR(50)
                )
                """;

        try (
                Connection connection = getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.execute();

            LOGGER.info("Table created successfully");

        } catch (SQLException e) {

            LOGGER.error("Failed to create table", e);

            throw new DatabaseException(
                    "Failed to create table",
                    e
            );
        }
    }

    /**
     * Save entity into database.
     */
    @Override
    public void save(QuantityMeasurementEntity quantityMeasurementEntity) {

        String sql = """
                INSERT INTO quantity_measurement_entity (
                    operand1_value,
                    operand1_unit,
                    operand2_value,
                    operand2_unit,
                    measurement_type,
                    operation_type,
                    result_value,
                    result_unit
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setDouble(
                    1,
                    quantityMeasurementEntity.getOperand1Value()
            );

            preparedStatement.setString(
                    2,
                    quantityMeasurementEntity.getOperand1Unit()
            );

            preparedStatement.setDouble(
                    3,
                    quantityMeasurementEntity.getOperand2Value()
            );

            preparedStatement.setString(
                    4,
                    quantityMeasurementEntity.getOperand2Unit()
            );

            preparedStatement.setString(
                    5,
                    quantityMeasurementEntity.getMeasurementType()
            );

            preparedStatement.setString(
                    6,
                    quantityMeasurementEntity.getOperationType()
            );

            preparedStatement.setDouble(
                    7,
                    quantityMeasurementEntity.getResultValue()
            );

            preparedStatement.setString(
                    8,
                    quantityMeasurementEntity.getResultUnit()
            );

            preparedStatement.executeUpdate();

            LOGGER.info("Entity saved successfully");

        } catch (SQLException e) {

            LOGGER.error("Failed to save entity", e);

            throw new DatabaseException(
                    "Failed to save entity",
                    e
            );
        }
    }

    /**
     * Get all measurements.
     */
    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {

        String sql =
                "SELECT * FROM quantity_measurement_entity";

        List<QuantityMeasurementEntity> measurements =
                new ArrayList<>();

        try (
                Connection connection = getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        preparedStatement.executeQuery()
        ) {

            while (resultSet.next()) {

                measurements.add(
                        mapResultSet(resultSet)
                );
            }

            LOGGER.info(
                    "Fetched all measurements successfully"
            );

            return measurements;

        } catch (SQLException e) {

            LOGGER.error(
                    "Failed to fetch measurements",
                    e
            );

            throw new DatabaseException(
                    "Failed to fetch measurements",
                    e
            );
        }
    }

    /**
     * Get measurements by operation type.
     */
    @Override
    public List<QuantityMeasurementEntity>
    getMeasurementsByOperation(String operationType) {

        String sql = """
                SELECT *
                FROM quantity_measurement_entity
                WHERE operation_type = ?
                """;

        List<QuantityMeasurementEntity> measurements =
                new ArrayList<>();

        try (
                Connection connection = getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    operationType
            );

            try (
                    ResultSet resultSet =
                            preparedStatement.executeQuery()
            ) {

                while (resultSet.next()) {

                    measurements.add(
                            mapResultSet(resultSet)
                    );
                }
            }

            LOGGER.info(
                    "Fetched measurements by operation type"
            );

            return measurements;

        } catch (SQLException e) {

            LOGGER.error(
                    "Failed to fetch by operation type",
                    e
            );

            throw new DatabaseException(
                    "Failed to fetch by operation type",
                    e
            );
        }
    }

    /**
     * Get measurements by measurement type.
     */
    @Override
    public List<QuantityMeasurementEntity>
    getMeasurementsByType(String measurementType) {

        String sql = """
                SELECT *
                FROM quantity_measurement_entity
                WHERE measurement_type = ?
                """;

        List<QuantityMeasurementEntity> measurements =
                new ArrayList<>();

        try (
                Connection connection = getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    measurementType
            );

            try (
                    ResultSet resultSet =
                            preparedStatement.executeQuery()
            ) {

                while (resultSet.next()) {

                    measurements.add(
                            mapResultSet(resultSet)
                    );
                }
            }

            LOGGER.info(
                    "Fetched measurements by type"
            );

            return measurements;

        } catch (SQLException e) {

            LOGGER.error(
                    "Failed to fetch by type",
                    e
            );

            throw new DatabaseException(
                    "Failed to fetch by type",
                    e
            );
        }
    }

    /**
     * Delete all measurements.
     */
    @Override
    public void deleteAll() {

        String sql =
                "DELETE FROM quantity_measurement_entity";

        try (
                Connection connection = getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.executeUpdate();

            LOGGER.info(
                    "Deleted all measurements"
            );

        } catch (SQLException e) {

            LOGGER.error(
                    "Failed to delete measurements",
                    e
            );

            throw new DatabaseException(
                    "Failed to delete measurements",
                    e
            );
        }
    }

    /**
     * Get total count.
     */
    @Override
    public long getTotalCount() {

        String sql =
                "SELECT COUNT(*) FROM quantity_measurement_entity";

        try (
                Connection connection = getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        preparedStatement.executeQuery()
        ) {

            if (resultSet.next()) {

                return resultSet.getLong(1);
            }

            return 0;

        } catch (SQLException e) {

            LOGGER.error(
                    "Failed to get total count",
                    e
            );

            throw new DatabaseException(
                    "Failed to get total count",
                    e
            );
        }
    }

    /**
     * Map ResultSet to Entity.
     */
    private QuantityMeasurementEntity
    mapResultSet(ResultSet resultSet)
            throws SQLException {

        QuantityMeasurementEntity quantityMeasurementEntity =
                new QuantityMeasurementEntity();

        quantityMeasurementEntity.setId(
                resultSet.getLong("id")
        );

        quantityMeasurementEntity.setOperand1Value(
                resultSet.getDouble("operand1_value")
        );

        quantityMeasurementEntity.setOperand1Unit(
                resultSet.getString("operand1_unit")
        );

        quantityMeasurementEntity.setOperand2Value(
                resultSet.getDouble("operand2_value")
        );

        quantityMeasurementEntity.setOperand2Unit(
                resultSet.getString("operand2_unit")
        );

        quantityMeasurementEntity.setMeasurementType(
                resultSet.getString("measurement_type")
        );

        quantityMeasurementEntity.setOperationType(
                resultSet.getString("operation_type")
        );

        quantityMeasurementEntity.setResultValue(
                resultSet.getDouble("result_value")
        );

        quantityMeasurementEntity.setResultUnit(
                resultSet.getString("result_unit")
        );

        return quantityMeasurementEntity;
    }

}
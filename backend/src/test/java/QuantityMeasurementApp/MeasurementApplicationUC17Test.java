package QuantityMeasurementApp;

import com.quantity.measurement.dto.QuantityDTO;
import com.quantity.measurement.dto.QuantityInputDTO;
import com.quantity.measurement.repository.QuantityMeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MeasurementApplicationUC17Test {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private QuantityMeasurementRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testRestEndpointAddQuantities() throws Exception {
        QuantityInputDTO input = input(
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                new QuantityDTO(12.0, "INCH", "LENGTH"),
                "FEET");
        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(2.0))
                .andExpect(jsonPath("$.unit").value("FEET"));
        mockMvc.perform(get("/api/v1/quantities/history/operation/ADD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].operationType").value("ADD"));
    }

    @Test
    void testRestEndpointConvertQuantitiesAcceptsPdfPayloadShape() throws Exception {
        QuantityInputDTO input = input(
                new QuantityDTO(1.0, "FEET", "LengthUnit"),
                new QuantityDTO(0.0, "INCHES", "LengthUnit"),
                null);
        mockMvc.perform(post("/api/v1/quantities/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(12.0))
                .andExpect(jsonPath("$.unit").value("INCHES"));
    }

    @Test
    void testInvalidInputReturns400() throws Exception {
        QuantityInputDTO input = input(
                new QuantityDTO(1.0, "INCHE", "LENGTH"),
                new QuantityDTO(1.0, "FEET", "LENGTH"),
                null);
        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Quantity Measurement Error"));
    }

    @Test
    void testDocumentationAndActuatorEndpointsLoad() throws Exception {
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").value("3.1.0"));
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.names.length()", greaterThanOrEqualTo(1)));
    }

    private QuantityInputDTO input(QuantityDTO first, QuantityDTO second, String targetUnit) {
        QuantityInputDTO input = new QuantityInputDTO();
        input.setThisQuantityDTO(first);
        input.setThatQuantityDTO(second);
        input.setTargetUnit(targetUnit);
        return input;
    }
}
package nl.avflexologic.wbje.InterGrationTest;

import nl.avflexologic.wbje.repositories.ReportRepository;
import nl.avflexologic.wbje.repositories.ReportSpecRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the ReportSpec HTTP API.
 *
 * This test suite runs against the real Spring Boot context
 * using the "test" profile.
 *
 * The database is reset before each test in the correct
 * referential order (child -> parent) to prevent FK violations.
 *
 * Tests follow the Arrange – Act – Assert pattern.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ReportSpecControllerIntegrationTest {

    private static final Pattern ID_PATTERN =
            Pattern.compile("\"id\"\\s*:\\s*(\\d+)");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportSpecRepository reportSpecRepository;

    @BeforeEach
    void resetDatabase() {
        /*
         * First delete child entities (reports),
         * then delete parent entities (report_specs),
         * to respect foreign key constraints.
         */
        reportRepository.deleteAll();
        reportSpecRepository.deleteAll();
    }

    @Test
    void createReportSpec_returnsPersistedReportSpec() throws Exception {

        // Arrange
        String requestJson = """
                {
                  "reportName": "Plate A",
                  "reportType": "Polymer",
                  "thickness": 67,
                  "info": "Default plate spec"
                }
                """;

        // Act
        String response = mockMvc.perform(post("/report-specs")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reportName", is("Plate A")))
                .andExpect(jsonPath("$.reportType", is("Polymer")))
                .andExpect(jsonPath("$.thickness", is(67)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Assert
        Matcher matcher = ID_PATTERN.matcher(response);
        assert matcher.find();
    }

    @Test
    void getReportSpecById_returnsReportSpec() throws Exception {

        // Arrange
        String requestJson = """
                {
                  "reportName": "Plate B",
                  "reportType": "Polymer",
                  "thickness": 80,
                  "info": "High density plate"
                }
                """;

        String createResponse = mockMvc.perform(post("/report-specs")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Matcher matcher = ID_PATTERN.matcher(createResponse);
        assert matcher.find();
        String id = matcher.group(1);

        // Act & Assert
        mockMvc.perform(get("/report-specs/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(id))))
                .andExpect(jsonPath("$.reportName", is("Plate B")))
                .andExpect(jsonPath("$.reportType", is("Polymer")))
                .andExpect(jsonPath("$.thickness", is(80)));
    }

    @Test
    void getAllReportSpecs_returnsList() throws Exception {

        // Arrange
        String json1 = """
                {
                  "reportName": "Plate 1",
                  "reportType": "Polymer",
                  "thickness": 70,
                  "info": "Spec 1"
                }
                """;

        String json2 = """
                {
                  "reportName": "Plate 2",
                  "reportType": "Rubber",
                  "thickness": 90,
                  "info": "Spec 2"
                }
                """;

        mockMvc.perform(post("/report-specs")
                .contentType(APPLICATION_JSON)
                .content(json1));

        mockMvc.perform(post("/report-specs")
                .contentType(APPLICATION_JSON)
                .content(json2));

        // Act & Assert
        mockMvc.perform(get("/report-specs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(2)));
    }

    @Test
    void deleteReportSpec_removesEntity() throws Exception {

        // Arrange
        String requestJson = """
                {
                  "reportName": "Plate Delete",
                  "reportType": "Polymer",
                  "thickness": 60,
                  "info": "To be deleted"
                }
                """;

        String response = mockMvc.perform(post("/report-specs")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Matcher matcher = ID_PATTERN.matcher(response);
        assert matcher.find();
        String id = matcher.group(1);

        // Act
        mockMvc.perform(delete("/report-specs/" + id))
                .andExpect(status().isNoContent());

        // Assert
        mockMvc.perform(get("/report-specs/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getReportSpec_notExisting_returns404() throws Exception {

        // Act & Assert
        mockMvc.perform(get("/report-specs/999999"))
                .andExpect(status().isNotFound());
    }
}

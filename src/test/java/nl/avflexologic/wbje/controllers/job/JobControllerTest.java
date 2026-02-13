package nl.avflexologic.wbje.controllers.job;

import nl.avflexologic.wbje.controllers.JobController;
import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
import nl.avflexologic.wbje.services.JobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web-layer test for {@link JobController}.
 * This test class verifies the HTTP contract of the JobController without
 * loading the full application context. Only request handling, routing,
 * serialization and response codes are validated.

 * Business logic, persistence and domain consistency are intentionally
 * excluded and delegated to dedicated service- and repository-level tests.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) //Dont forget to turn off the auth!! :S
class JobControllerTest {

    /**
     * MockMvc provides a programmatic entry point to the Spring MVC layer.
     *
     * Requests are executed against the controller as if they were real HTTP
     * calls, but without starting an embedded server.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Test-specific configuration supplying a minimal JobService implementation.
     * The implementation returns deterministic data to ensure stable and
     * predictable assertions.
     */
    @TestConfiguration
    static class TestConfig {

        @Bean
        JobService jobService() {
            return new JobService() {

                /**
                 * Simulates job creation by echoing request data into a response DTO.
                 *
                 * No validation, persistence or side effects are performed here.
                 * The sole purpose is to satisfy the controller dependency.
                 */
                @Override
                public JobResponseDTO createJob(JobRequestDTO request) {
                    JobResponseDTO response = new JobResponseDTO();
                    response.id = 1L;
                    response.jobNumber = request.getJobNumber();
                    response.jobDate = request.getJobDate();
                    response.jobName = request.getJobName();
                    response.info = request.getInfo();
                    response.cylinderWidth = request.getCylinderWidth();
                    response.cylinderCircumference = request.getCylinderCircumference();
                    response.noteInfo = request.getNoteInfo();
                    return response;
                }

                /**
                 * Returns a fixed job representation for retrieval by id.
                 *
                 * The returned data is independent of the provided identifier,
                 * as identifier-based correctness is verified at the service layer.
                 */
                @Override
                public JobResponseDTO getJobById(Long id) {
                    JobResponseDTO response = new JobResponseDTO();
                    response.id = id;
                    response.jobNumber = "JB-1001";
                    response.jobDate = LocalDateTime.parse("2025-12-22T10:00:00");
                    response.jobName = "WBJE Demo";
                    response.info = "Initial setup for customer order.";
                    response.cylinderWidth = 120;
                    response.cylinderCircumference = 800;
                    response.noteInfo = "Note text";
                    return response;
                }

                /**
                 * Returns a fixed list of jobs to validate collection endpoints.
                 *
                 * The size and ordering are deterministic to allow explicit
                 * assertions on list content.
                 */
                @Override
                public List<JobResponseDTO> getAllJobs() {
                    JobResponseDTO job1 = new JobResponseDTO();
                    job1.id = 1L;
                    job1.jobNumber = "JB-1001";
                    job1.jobDate = LocalDateTime.parse("2025-12-22T10:00:00");
                    job1.jobName = "WBJE Demo";
                    job1.info = "Initial setup for customer order.";
                    job1.cylinderWidth = 120;
                    job1.cylinderCircumference = 800;
                    job1.noteInfo = "Note text";

                    JobResponseDTO job2 = new JobResponseDTO();
                    job2.id = 2L;
                    job2.jobNumber = "JB-1002";
                    job2.jobDate = LocalDateTime.parse("2025-12-23T09:30:00");
                    job2.jobName = "WBJE Production";
                    job2.info = "Second production run.";
                    job2.cylinderWidth = 130;
                    job2.cylinderCircumference = 820;
                    job2.noteInfo = null;

                    return List.of(job1, job2);
                }

                /**
                 * Simulates updating a job by returning the provided request data.
                 *
                 * The identifier is preserved to reflect update semantics.
                 */
                @Override
                public JobResponseDTO updateJob(Long id, JobRequestDTO request) {
                    JobResponseDTO response = new JobResponseDTO();
                    response.id = id;
                    response.jobNumber = request.getJobNumber();
                    response.jobDate = request.getJobDate();
                    response.jobName = request.getJobName();
                    response.info = request.getInfo();
                    response.cylinderWidth = request.getCylinderWidth();
                    response.cylinderCircumference = request.getCylinderCircumference();
                    response.noteInfo = request.getNoteInfo();
                    return response;
                }

                /**
                 * No-op delete implementation.
                 *
                 * Successful execution indicates that the controller endpoint
                 * correctly delegates deletion requests.
                 */
                @Override
                public void deleteJob(Long id) {
                    // intentionally left blank
                }
            };
        }
    }

    /**
     * Verifies that a job can be created via POST /jobs and that
     * a valid JSON response is returned.
     */
    @Test
    void createJob_returnsOkAndJobBody() throws Exception {
        String jsonRequest = """
                {
                  "jobNumber": "JB-1001",
                  "jobDate": "2025-12-22T10:00:00",
                  "jobName": "WBJE Demo",
                  "info": "Initial setup for customer order.",
                  "cylinderWidth": 120,
                  "cylinderCircumference": 800,
                  "noteInfo": "Note text"
                }
                """;

        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.jobNumber").value("JB-1001"))
                .andExpect(jsonPath("$.jobName").value("WBJE Demo"));
    }

    /**
     * Verifies retrieval of a single job by identifier.
     */
    @Test
    void getJobById_returnsOkAndJobBody() throws Exception {
        mockMvc.perform(get("/jobs/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.jobNumber").value("JB-1001"));
    }

    /**
     * Verifies retrieval of all jobs.
     */
    @Test
    void getAllJobs_returnsOkAndList() throws Exception {
        mockMvc.perform(get("/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    /**
     * Verifies that updating an existing job returns the modified representation.
     */
    @Test
    void updateJob_returnsOkAndUpdatedBody() throws Exception {
        String jsonRequest = """
                {
                  "jobNumber": "JB-1001",
                  "jobDate": "2025-12-22T10:00:00",
                  "jobName": "WBJE Demo (updated)",
                  "info": "Updated info after customer feedback.",
                  "cylinderWidth": 120,
                  "cylinderCircumference": 800,
                  "noteInfo": "Updated note text"
                }
                """;

        mockMvc.perform(put("/jobs/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobName").value("WBJE Demo (updated)"))
                .andExpect(jsonPath("$.noteInfo").value("Updated note text"));
    }

    /**
     * Verifies that a job can be deleted via DELETE /jobs/{id}.
     */
    @Test
    void deleteJob_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/jobs/{id}", 1))
                .andExpect(status().isNoContent());
    }
}

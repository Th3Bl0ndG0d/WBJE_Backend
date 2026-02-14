package nl.avflexologic.wbje.InterGrationTest;

import nl.avflexologic.wbje.entities.JobEntity;
import nl.avflexologic.wbje.repositories.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Integration tests for the Job HTTP API.
 *
 * The suite runs against a real Spring Boot context using the "test" profile.
 * Tests follow AAA (Arrange, Act, Assert) and avoid object binding on purpose.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class JobControllerIntegrationTest {

    private static final Pattern ID_PATTERN = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobRepository jobRepository;

    @BeforeEach
    void resetDatabase() {
        /*
         * A clean database state prevents coupling across tests.
         * The id sequence is not assumed to restart; ids are handled dynamically.
         */
        jobRepository.deleteAll();
    }

    @Test
    void createJob_returnsPersistedJob() throws Exception {
        // Arrange
        String requestJson = """
            {
              "jobNumber": "JOB-2025-001",
              "jobDate": "2025-12-22T10:00:00",
              "jobName": "Print job for customer X",
              "cylinderWidth": 850,
              "cylinderCircumference": 1300,
              "info": "Operator note: validate substrate batch before start.",
              "note": {
                "content": "Repeat run, verify plate wear on station 2."
              }
            }
            """;

        // Act
        MvcResult postResult = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/jobs")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.jobNumber", is("JOB-2025-001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.jobName", is("Print job for customer X")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cylinderWidth", is(850)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cylinderCircumference", is(1300)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.info", is("Operator note: validate substrate batch before start.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.note").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.note.content",
                        is("Repeat run, verify plate wear on station 2.")))
                .andReturn();

        long id = extractIdFromJson(postResult.getResponse().getContentAsString());

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/jobs/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is((int) id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.jobNumber", is("JOB-2025-001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.jobName", is("Print job for customer X")));
    }


    @Test
    void getJobById_returnsExpectedPayload() throws Exception {
        // Arrange
        JobEntity job = new JobEntity(LocalDateTime.of(2025, 12, 22, 10, 0));
        job.setJobNumber("JOB-2025-002");
        job.setJobName("Seeded job");
        job.setCylinderWidth(900);
        job.setCylinderCircumference(1400);
        job.setInfo("Seeded info");
        JobEntity saved = jobRepository.save(job);

        long id = saved.getId();

        // Act + Assert
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/jobs/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is((int) id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.jobNumber", is("JOB-2025-002")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.jobName", is("Seeded job")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cylinderWidth", is(900)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cylinderCircumference", is(1400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.info", is("Seeded info")));
    }

    @Test
    void getJobById_unknownIdGives404() throws Exception {
        // Arrange
        // No database seeding is performed.

        // Act + Assert
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/jobs/{id}", 999))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", is("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Job not found for id: 999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path", is("/jobs/999")));
    }

    @Test
    void getAllJobs_returnsList() throws Exception {
        // Arrange
        JobEntity job1 = new JobEntity(LocalDateTime.of(2025, 12, 22, 10, 0));
        job1.setJobNumber("JOB-A");
        job1.setJobName("A");
        job1.setCylinderWidth(800);
        job1.setCylinderCircumference(1200);
        jobRepository.save(job1);

        JobEntity job2 = new JobEntity(LocalDateTime.of(2025, 12, 23, 11, 0));
        job2.setJobNumber("JOB-B");
        job2.setJobName("B");
        job2.setCylinderWidth(850);
        job2.setCylinderCircumference(1300);
        jobRepository.save(job2);

        // Act + Assert
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/jobs"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].jobNumber", is("JOB-A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].jobNumber", is("JOB-B")));
    }

    @Test
    void deleteJob_existingIdRemovesEntity() throws Exception {
        // Arrange
        JobEntity job = new JobEntity(LocalDateTime.of(2025, 12, 24, 12, 0));
        job.setJobNumber("JOB-DELETE");
        job.setJobName("Delete me");
        job.setCylinderWidth(800);
        job.setCylinderCircumference(1200);
        JobEntity saved = jobRepository.save(job);

        long id = saved.getId();

        // Act
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/jobs/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Assert
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/jobs/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteJob_unknownIdGives404() throws Exception {
        // Arrange
        // no seeding

        // Act + Assert
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/jobs/{id}", 999))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", is("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Job not found for id: 999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path", is("/jobs/999")));
    }

    @Test
    void createJob_invalidPayloadGives400() throws Exception {
        // Arrange
        String invalidJson = """
            {
              "jobNumber": "",
              "jobDate": null,
              "jobName": "",
              "cylinderWidth": 0,
              "cylinderCircumference": 0
            }
            """;

        // Act + Assert
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/jobs")
                        .contentType(APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", is("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path", is("/jobs")));
    }
    @Test
    void getAllJobs_whenEmptyReturnsEmptyArray() throws Exception {
        // Arrange
        // database is empty because of @BeforeEach

        // Act + Assert
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/jobs"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", is(0)));
    }

    @Test
    void updateJob_existingIdUpdatesEntity() throws Exception {
        // Arrange
        JobEntity job = new JobEntity(LocalDateTime.of(2025, 12, 22, 10, 0));
        job.setJobNumber("JOB-UPDATE");
        job.setJobName("Original");
        job.setCylinderWidth(800);
        job.setCylinderCircumference(1200);
        JobEntity saved = jobRepository.save(job);

        long id = saved.getId();

        String updateJson = """
            {
              "jobNumber": "JOB-UPDATE",
              "jobDate": "2025-12-22T10:00:00",
              "jobName": "Updated name",
              "cylinderWidth": 900,
              "cylinderCircumference": 1300,
              "info": "Updated info"
            }
            """;

        // Act
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/jobs/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content(updateJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jobName", is("Updated name")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cylinderWidth", is(900)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cylinderCircumference", is(1300)));
    }



    private long extractIdFromJson(String json) {
        Matcher matcher = ID_PATTERN.matcher(json);
        if (!matcher.find()) {
            throw new AssertionError("No 'id' field found in response body: " + json);
        }
        return Long.parseLong(matcher.group(1));
    }
}

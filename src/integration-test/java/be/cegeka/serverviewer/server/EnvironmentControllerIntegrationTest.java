package be.cegeka.serverviewer.server;

import be.cegeka.serverviewer.config.Application;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("environments.xml")
public class EnvironmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllEnvironments() throws Exception {
        Environment environmentAcc = new Environment("ACC");
        Environment environmentTest = new Environment("TST");

        mockMvc.perform(get("/server/environment")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("environment/list"))
                .andExpect(model().attribute("environments", hasItems(environmentAcc, environmentTest)));
    }

    @Test
    public void findEnvironmentById() throws Exception {
        mockMvc.perform(get("/server/environment/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("environment/view"))
                .andExpect(model().attribute("environment", hasProperty("id", is(1L))))
                .andExpect(model().attribute("environment", hasProperty("name", is("ACC"))));
    }

    @Test
    public void testCreateEnvironment() throws Exception {
        Environment environment = new Environment("Test");
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();

        mockMvc.perform(post("/server/environment/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(environment)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/server/environment"));
    }

    public static String convertObjectToJsonString(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }


}

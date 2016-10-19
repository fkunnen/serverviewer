package be.cegeka.serverviewer.servers;

import be.cegeka.serverviewer.config.Application;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
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
    public void testGetHomePage() throws Exception {
        mockMvc.perform(get("/")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void testGetAllEnvironments() throws Exception {
        Environment environmentAcc = new Environment("ACC");
        Environment environmentTest = new Environment("TST");

        mockMvc.perform(get("/servers/environment")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("environment/environment"))
                .andExpect(model().attribute("environments", hasItems(environmentAcc, environmentTest)));
    }

    @Test
    public void findEnvironmentById() throws Exception {
        mockMvc.perform(get("/servers/environment/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("environment/view"))
                .andExpect(model().attribute("environment", hasProperty("id", is(1L))))
                .andExpect(model().attribute("environment", hasProperty("name", is("ACC"))));
    }

    @Test
    public void testCreateEnvironmentForm() throws Exception {
        Environment emptyEnvironment = new Environment();

        mockMvc.perform(get("/servers/environment/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("environment/createEditEnvironment"))
                .andExpect(model().attribute("environment", is(emptyEnvironment)));
    }

    @Test
    public void testCreateEnvironment() throws Exception {

        mockMvc.perform(post("/servers/environment/create")
                .param("name", "CI")
                .param("description", "Continuous integration environment")
                .sessionAttr("environment", new Environment()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/environment"));
    }

}

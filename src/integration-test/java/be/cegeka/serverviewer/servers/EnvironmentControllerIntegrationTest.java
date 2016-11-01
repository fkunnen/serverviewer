package be.cegeka.serverviewer.servers;

import be.cegeka.serverviewer.config.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class EnvironmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Test
    public void testGetHomePage() throws Exception {
        mockMvc.perform(get("/")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void testGetAllEnvironments() throws Exception {
        environmentRepository.save(new Environment("ACC"));
        environmentRepository.save(new Environment("TST"));

        mockMvc.perform(get("/servers/environment")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("environment/environment"))
                .andExpect(model().attribute("environments", hasItems(new Environment("ACC"), new Environment("TST"))));
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
                .param("description", "Continuous integration environment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/environment"));
    }

    @Test
    public void editEnvironmentForm() throws Exception {
        Environment environment = environmentRepository.save(new Environment("ACC"));

        mockMvc.perform(get("/servers/environment/{id}", environment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("environment/createEditEnvironment"))
                .andExpect(model().attribute("environment", hasProperty("id", is(environment.getId()))))
                .andExpect(model().attribute("environment", hasProperty("name", is(environment.getName()))));
    }

    @Test
    public void testEditEnvironment() throws Exception {
        Environment environment = environmentRepository.save(new Environment("ACC"));

        mockMvc.perform(put("/servers/environment/" + environment.getId())
                .param("name", "ACC")
                .param("description", "Edit: ACC environment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/environment"));
    }

    @Test
    public void testDeleteEnvironment() throws Exception {
        Environment environment = environmentRepository.save(new Environment("ACC"));

        mockMvc.perform(get("/servers/environment/" + environment.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/environment"));
    }

}

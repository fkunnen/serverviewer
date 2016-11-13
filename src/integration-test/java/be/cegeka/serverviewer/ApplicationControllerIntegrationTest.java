package be.cegeka.serverviewer;

import be.cegeka.serverviewer.applications.Application;
import be.cegeka.serverviewer.applications.ApplicationRepository;
import be.cegeka.serverviewer.config.SpringWebApplication;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("applications.xml")
public class ApplicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllApplications() throws Exception {

        mockMvc.perform(get("/applications")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("application/application"))
                .andExpect(model().attribute("applications", hasItems(new Application("eDossier"), new Application("Postkamer"))));
    }

    @Test
    public void testCreateApplicationForm() throws Exception {

        mockMvc.perform(get("/applications/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("application/createEditApplication"))
                .andExpect(model().attribute("app", is(new Application())));
    }

    @Test
    public void testCreateApplication() throws Exception {

        mockMvc.perform(post("/applications/create")
                .param("name", "B2B")
                .param("description", "B2B Application"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/applications"));
    }

    @Test
    public void editApplicationForm() throws Exception {

        mockMvc.perform(get("/applications/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("application/createEditApplication"))
                .andExpect(model().attribute("app", hasProperty("id", is(1L))))
                .andExpect(model().attribute("app", hasProperty("name", is("eDossier"))));
    }

    @Test
    public void testEditApplication() throws Exception {

        mockMvc.perform(put("/applications/2")
                .param("name", "Postkamer")
                .param("description", "Edit: Postkamer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/applications"));
    }

    @Test
    public void testDeleteApplication() throws Exception {

        mockMvc.perform(get("/applications/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/applications"));
    }
}

package be.cegeka.serverviewer.servers.operatingsystem;

import be.cegeka.serverviewer.config.SpringWebApplication;
import be.cegeka.serverviewer.servers.operatingsystem.OperatingSystem;
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
@DatabaseSetup("operatingsystems.xml")
public class OperatingSystemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllOperatingSystems() throws Exception {

        mockMvc.perform(get("/servers/operatingsystem")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("servers/operatingsystem/operatingsystem"))
                .andExpect(model().attribute("operatingSystems", hasItems(new OperatingSystem("RHEL 5 (32 bit)"), new OperatingSystem("Windows Server 2008 R2 standard"))));
    }

    @Test
    public void testCreateOperatingSystemForm() throws Exception {
        OperatingSystem emptyOperatingSystem = new OperatingSystem();

        mockMvc.perform(get("/servers/operatingsystem/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("servers/operatingsystem/createEditOperatingSystem"))
                .andExpect(model().attribute("operatingSystem", is(emptyOperatingSystem)));
    }

    @Test
    public void testCreateOperatingSystem() throws Exception {

        mockMvc.perform(post("/servers/operatingsystem/create")
                .param("name", "RHEL 6 (64 bit)")
                .param("description", "Redhat Enterprise Linux 6 (64 bit)"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/operatingsystem"));
    }

    @Test
    public void editOperatingSystemForm() throws Exception {

        mockMvc.perform(get("/servers/operatingsystem/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("servers/operatingsystem/createEditOperatingSystem"))
                .andExpect(model().attribute("operatingSystem", hasProperty("id", is(1L))))
                .andExpect(model().attribute("operatingSystem", hasProperty("name", is("RHEL 5 (32 bit)"))));
    }

    @Test
    public void testEditOperatingSystem() throws Exception {

        mockMvc.perform(put("/servers/operatingsystem/2")
                .param("name", "Windows Server 2008 R2 standard")
                .param("description", "Edit: Windows Server 2008 R2 standard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/operatingsystem"));
    }

    @Test
    public void testDeleteOperatingSystem() throws Exception {

        mockMvc.perform(get("/servers/operatingsystem/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/operatingsystem"));
    }
}

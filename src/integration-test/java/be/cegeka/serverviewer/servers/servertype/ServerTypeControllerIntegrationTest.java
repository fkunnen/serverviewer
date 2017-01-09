package be.cegeka.serverviewer.servers.servertype;

import be.cegeka.serverviewer.config.SpringWebApplication;
import be.cegeka.serverviewer.servers.servertype.ServerType;
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
@DatabaseSetup("servertypes.xml")
public class ServerTypeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllServerTypes() throws Exception {

        mockMvc.perform(get("/servers/servertype")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("servers/servertype/servertype"))
                .andExpect(model().attribute("serverTypes", hasItems(new ServerType("Hardware Server"), new ServerType("Virtual Server"))));
    }

    @Test
    public void testCreateServerTypeForm() throws Exception {
        ServerType emptyServerType = new ServerType();

        mockMvc.perform(get("/servers/servertype/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("servers/servertype/createEditServerType"))
                .andExpect(model().attribute("serverType", is(emptyServerType)));
    }

    @Test
    public void testCreateServerType() throws Exception {

        mockMvc.perform(post("/servers/servertype/create")
                .param("name", "Database")
                .param("description", "Database server"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/servertype"));
    }

    @Test
    public void editServerTypeForm() throws Exception {

        mockMvc.perform(get("/servers/servertype/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("servers/servertype/createEditServerType"))
                .andExpect(model().attribute("serverType", hasProperty("id", is(1L))))
                .andExpect(model().attribute("serverType", hasProperty("name", is("Hardware Server"))));
    }

    @Test
    public void testEditServerType() throws Exception {

        mockMvc.perform(put("/servers/servertype/2")
                .param("name", "Virtual Server")
                .param("description", "Virtual server"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/servertype"));
    }

    @Test
    public void testDeleteServerType() throws Exception {

        mockMvc.perform(get("/servers/servertype/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/servertype"));
    }
}

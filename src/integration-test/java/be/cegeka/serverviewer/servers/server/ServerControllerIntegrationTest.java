package be.cegeka.serverviewer.servers.server;

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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;

import static be.cegeka.serverviewer.common.MockHttpServletPerformPostRequestBuilder.postForm;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("servers.xml")
public class ServerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllServers() throws Exception {

        mockMvc.perform(get("/servers/server")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("servers/server/server"))
                .andExpect(model().attribute("servers", hasItems(new Server("B2B PRD 1"), new Server("B2B PRD 2"))));
    }

    @Test
    public void testCreateServerForm() throws Exception {
        Server emptyServer = new Server();

        mockMvc.perform(get("/servers/server/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("servers/server/createEditServer"))
                .andExpect(model().attribute("server", is(emptyServer)));
    }

    @Test
    public void testCreateServer() throws Exception {
        Server server = new ServerTestBuilder()
                .withName("B2B ACC 1")
                .withCode("HI08553")
                .withHostname("SVRSVZAB2BAPP01")
                .withDescription("B2B Acceptance Managed Server 1")
                .build();

        String[] propertyPaths = {"name", "serverType.name", "code", "hostname", "description", "location.name", "environment.name", "operatingSystem.name"};

        mockMvc.perform(postForm("/servers/server/create", server, propertyPaths))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/server"));
    }

    @Test
    public void editServerForm() throws Exception {

        mockMvc.perform(get("/servers/server/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("servers/server/createEditServer"))
                .andExpect(model().attribute("server", hasProperty("id", is(1L))))
                .andExpect(model().attribute("server", hasProperty("name", is("B2B PRD 1"))));
    }

    @Test
    public void testEditServer() throws Exception {

        mockMvc.perform(put("/servers/server/2")
                .param("name", "CGK.Hasselt")
                .param("description", "Edit: Cegeka Hasselt"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/server"));
    }

    @Test
    public void testDeleteServer() throws Exception {

        mockMvc.perform(get("/servers/server/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/server"));
    }

}

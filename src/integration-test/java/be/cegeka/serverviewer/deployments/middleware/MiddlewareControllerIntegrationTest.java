package be.cegeka.serverviewer.deployments.middleware;

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
@DatabaseSetup("middlewares.xml")
public class MiddlewareControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllMiddlewares() throws Exception {

        mockMvc.perform(get("/deployments/middleware")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("deployments/middleware/middleware"))
                .andExpect(model().attribute("middlewares", hasItems(new Middleware("Weblogic 12c"), new Middleware("Weblogic 11g"))));
    }

    @Test
    public void testCreateMiddlewareForm() throws Exception {
        Middleware emptyMiddleware = new Middleware();

        mockMvc.perform(get("/deployments/middleware/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("deployments/middleware/createEditMiddleware"))
                .andExpect(model().attribute("middleware", is(emptyMiddleware)));
    }

    @Test
    public void testCreateMiddleware() throws Exception {

        mockMvc.perform(post("/deployments/middleware/create")
                .param("name", "Tomcat 8")
                .param("description", "Apache Tomcat 8"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/deployments/middleware"));
    }

    @Test
    public void editMiddlewareForm() throws Exception {

        mockMvc.perform(get("/deployments/middleware/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("deployments/middleware/createEditMiddleware"))
                .andExpect(model().attribute("middleware", hasProperty("id", is(1L))))
                .andExpect(model().attribute("middleware", hasProperty("name", is("Weblogic 12c"))));
    }

    @Test
    public void testEditMiddleware() throws Exception {

        mockMvc.perform(put("/deployments/middleware/2")
                .param("name", "Weblogic 11g")
                .param("description", "Edit: Oracle WebLogic Server 11g (10.3.6)"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/deployments/middleware"));
    }

    @Test
    public void testDeleteMiddleware() throws Exception {

        mockMvc.perform(get("/deployments/middleware/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/deployments/middleware"));
    }
}

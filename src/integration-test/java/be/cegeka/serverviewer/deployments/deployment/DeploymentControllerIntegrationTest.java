package be.cegeka.serverviewer.deployments.deployment;


import be.cegeka.serverviewer.config.SpringWebApplication;
import be.cegeka.serverviewer.deployments.middleware.Middleware;
import be.cegeka.serverviewer.servers.server.Server;
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

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("deployments.xml")
public class DeploymentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllMiddlewares() throws Exception {

        mockMvc.perform(get("/deployments/deployment")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("deployments/deployment/deployment"))
                .andExpect(model().attribute("deployment", hasItems(new Middleware("Weblogic 12c"), new Middleware("Weblogic 11g"))));
    }

    private Deployment b2bApplicationBuiltOnPrd1(){
        return new DeploymentTestBuilder().build();
    }

    private Deployment b2bApplicationBuiltOnPrd2(){
        Server server = new Server("B2B PRD 2");
        return new DeploymentTestBuilder().withServer(server).build();
    }

}

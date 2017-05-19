package be.cegeka.serverviewer.deployments.deployment;


import be.cegeka.serverviewer.applications.Application;
import be.cegeka.serverviewer.applications.ApplicationRepository;
import be.cegeka.serverviewer.config.SpringWebApplication;
import be.cegeka.serverviewer.deployments.middleware.Middleware;
import be.cegeka.serverviewer.servers.server.Server;
import be.cegeka.serverviewer.servers.server.ServerTestBuilder;
import be.cegeka.serverviewer.servers.servertype.ServerType;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.hamcrest.Matchers;
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

import java.util.List;

import static be.cegeka.serverviewer.common.MockHttpServletFormRequestBuilder.postForm;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    public void testGetAllDeployments() throws Exception {

        mockMvc.perform(get("/deployments/deployment")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("deployments/deployment/deployment"))
                .andExpect(model().attribute("deployments", hasItems(b2bApplicationOnPrd1(), b2bApplicationOnPrd2())));
    }

    @Test
    public void testCreateDeploymentForm() throws Exception {
        Deployment deployment = new Deployment();

        mockMvc.perform(get("/deployments/deployment/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("deployments/deployment/createEditDeployment"))
                .andExpect(model().attribute("deployment", is(deployment)))
                .andExpect(model().attribute("applications", hasItem(new Application("B2B"))))
                .andExpect(model().attribute("servers", hasItems(new Server("B2B PRD 1"), new Server("B2B PRD 2"))))
                .andExpect(model().attribute("middlewares", hasItem(new Middleware("Weblogic 11g"))));
    }

    @Test
    public void testCreateDeployment() throws Exception {
        Deployment deployment = b2bApplicationOnAcc1();

        mockMvc.perform(postForm("/deployments/deployment/create", deployment, getDeploymentPropertyPaths()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/deployments/deployment"));
    }

    private Deployment b2bApplicationOnPrd1(){
        return new DeploymentTestBuilder().build();
    }

    private Deployment b2bApplicationOnPrd2(){
        Server server = new Server("B2B PRD 2");
        return new DeploymentTestBuilder().withServer(server).build();

    }

    private Deployment b2bApplicationOnAcc1(){
        Server server = new Server("B2B ACC 2");
        return new DeploymentTestBuilder()
                .withServer(server)
                .build();
    }

    private String[] getDeploymentPropertyPaths() {
        return new String[]{"application.name", "server.name", "middleware.name", "dockerized", "applicationUrl"};
    }

}

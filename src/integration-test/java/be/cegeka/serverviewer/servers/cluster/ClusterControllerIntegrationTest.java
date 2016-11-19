package be.cegeka.serverviewer.servers.cluster;

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

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("clusters.xml")
public class ClusterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllClusters() throws Exception {

        mockMvc.perform(get("/servers/cluster")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/cluster"))
                .andExpect(model().attribute("clusters", hasItems(new Cluster("B2B PRD Cluster"), new Cluster("B2B ACC Cluster"))));
    }

    @Test
    public void testCreateClusterForm() throws Exception {
        Cluster emptyCluster = new Cluster();

        mockMvc.perform(get("/servers/cluster/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/createEditCluster"))
                .andExpect(model().attribute("cluster", is(emptyCluster)));
    }

    @Test
    public void testCreateCluster() throws Exception {

        mockMvc.perform(post("/servers/cluster/create")
                .param("name", "eDossier TST Cluster")
                .param("description", "eDossier test cluster"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/cluster"));
    }

    @Test
    public void editClusterForm() throws Exception {

        mockMvc.perform(get("/servers/cluster/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("cluster/createEditCluster"))
                .andExpect(model().attribute("cluster", hasProperty("id", is(1L))))
                .andExpect(model().attribute("cluster", hasProperty("name", is("B2B PRD Cluster"))));
    }

    @Test
    public void testEditCluster() throws Exception {

        mockMvc.perform(put("/servers/cluster/2")
                .param("name", "B2B ACC Cluster")
                .param("description", "Edit: B2B acceptance cluster"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/cluster"));
    }

    @Test
    public void testDeleteCluster() throws Exception {

        mockMvc.perform(get("/servers/cluster/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/cluster"));
    }
}

package be.cegeka.serverviewer.servers;

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
@DatabaseSetup("locations.xml")
public class LocationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllLocations() throws Exception {

        mockMvc.perform(get("/servers/location")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("location/location"))
                .andExpect(model().attribute("locations", hasItems(new Location("CGK.Leuven"), new Location("CGK.Hasselt"))));
    }

    @Test
    public void testCreateLocationForm() throws Exception {
        Location emptyLocation = new Location();

        mockMvc.perform(get("/servers/location/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("location/createEditLocation"))
                .andExpect(model().attribute("location", is(emptyLocation)));
    }

    @Test
    public void testCreateLocation() throws Exception {

        mockMvc.perform(post("/servers/location/create")
                .param("name", "RSVZ")
                .param("description", "RSVZ Brussel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/location"));
    }

    @Test
    public void editLocationForm() throws Exception {

        mockMvc.perform(get("/servers/location/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("location/createEditLocation"))
                .andExpect(model().attribute("location", hasProperty("id", is(1L))))
                .andExpect(model().attribute("location", hasProperty("name", is("CGK.Leuven"))));
    }

    @Test
    public void testEditLocation() throws Exception {

        mockMvc.perform(put("/servers/location/2")
                .param("name", "CGK.Hasselt")
                .param("description", "Edit: Cegeka Hasselt"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/location"));
    }

    @Test
    public void testDeleteLocation() throws Exception {

        mockMvc.perform(get("/servers/location/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servers/location"));
    }
}

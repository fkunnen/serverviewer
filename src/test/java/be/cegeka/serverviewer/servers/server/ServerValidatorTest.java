package be.cegeka.serverviewer.servers.server;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

public class ServerValidatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private ServerValidator serverValidator = new ServerValidator();

    @Mock
    private ServerRepository serverRepository;

    private ServerTestBuilder serverTestBuilder;

    @Before
    public void setUp() throws Exception {
        serverTestBuilder = new ServerTestBuilder();
    }

    @Test
    public void testSupports_ServerTypeClassReturnsTrue(){
        boolean isAssignableFrom = serverValidator.supports(Server.class);
        Assertions.assertThat(isAssignableFrom).isTrue();
    }

    @Test
    public void testValidate_isValid(){
        Server server = serverTestBuilder.build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        when(serverRepository.findByName(server.getName())).thenReturn(Collections.EMPTY_LIST);

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void testValidate_ServerNameIsMandatory(){
        Server server = serverTestBuilder.withName("").build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        when(serverRepository.findByName(server.getName())).thenReturn(Collections.EMPTY_LIST);

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("Server name is mandatory");
    }

    @Test
    public void testValidate_ServerAlreadyExists(){
        Server server = serverTestBuilder.build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        List<Server> currentServers = Arrays.asList(server);
        when(serverRepository.findByName(server.getName())).thenReturn(currentServers);

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("A server with name 'B2B PRD 1' already exists");
    }

    @Test
    public void testValidate_ServerNameTooLong(){
        Server server = serverTestBuilder.withName("The name for this server is more than 45 characters, the maximum size for a server name").build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        when(serverRepository.findByName(server.getName())).thenReturn(Collections.EMPTY_LIST);

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("The name of this server is too long");
    }

    @Test
    public void testValidate_ServerTypeDescriptionTooLong(){
        Server server = serverTestBuilder.withDescription("This server can't be described in less than one hundred characters, the maximum size for a server description").build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        when(serverRepository.findByName(server.getName())).thenReturn(Collections.EMPTY_LIST);

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("description");
        Assertions.assertThat(errors.getFieldError("description").getDefaultMessage()).isEqualTo("The description of this server is too long");
    }


}
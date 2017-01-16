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

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void testValidate_ServerTypeIsMandatory(){
        Server server = serverTestBuilder.withServerType(null).build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("serverType");
        Assertions.assertThat(errors.getFieldError("serverType").getDefaultMessage()).isEqualTo("Server type is mandatory");
    }

    @Test
    public void testValidate_ServerNameIsMandatory(){
        Server server = serverTestBuilder.withName("").build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("Server name is mandatory");
    }

    @Test
    public void testValidate_ServerAlreadyExists(){
        Server server = serverTestBuilder.build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        List<Server> currentServers = Collections.singletonList(server);
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

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("The name of this server is too long");
    }

    @Test
    public void testValidate_ServerCodeIsMandatory(){
        Server server = serverTestBuilder.withCode("").build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("code");
        Assertions.assertThat(errors.getFieldError("code").getDefaultMessage()).isEqualTo("Server code is mandatory");
    }

    @Test
    public void testValidate_ServerCodeAlreadyExists(){
        Server server = serverTestBuilder.build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        List<Server> currentServers = Collections.singletonList(server);
        when(serverRepository.findByCode(server.getCode())).thenReturn(currentServers);

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("code");
        Assertions.assertThat(errors.getFieldError("code").getDefaultMessage()).isEqualTo("A server with code 'HI08553' already exists");
    }

    @Test
    public void testValidate_ServerCodeTooLong(){
        Server server = serverTestBuilder.withCode("The code for this server is more than 45 characters, the maximum size for a server name").build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("code");
        Assertions.assertThat(errors.getFieldError("code").getDefaultMessage()).isEqualTo("The code of this server is too long");
    }

    @Test
    public void testValidate_ServerHostnameIsMandatory(){
        Server server = serverTestBuilder.withHostname("").build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("hostname");
        Assertions.assertThat(errors.getFieldError("hostname").getDefaultMessage()).isEqualTo("Server hostname is mandatory");
    }

    @Test
    public void testValidate_ServerHostnameAlreadyExists(){
        Server server = serverTestBuilder.build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        List<Server> currentServers = Collections.singletonList(server);
        when(serverRepository.findByHostname(server.getHostname())).thenReturn(currentServers);

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("hostname");
        Assertions.assertThat(errors.getFieldError("hostname").getDefaultMessage()).isEqualTo("A server with hostname 'SVRSVZPB2BAPP01' already exists");
    }

    @Test
    public void testValidate_ServerHostnameTooLong(){
        Server server = serverTestBuilder.withHostname("The hostname for this server is more than 45 characters, the maximum size for a server name").build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("hostname");
        Assertions.assertThat(errors.getFieldError("hostname").getDefaultMessage()).isEqualTo("The hostname of this server is too long");
    }

    @Test
    public void testValidate_ServerDescriptionTooLong(){
        Server server = serverTestBuilder.withDescription("This server can't be described in less than one hundred characters, the maximum size for a server description").build();
        Errors errors = new BeanPropertyBindingResult(server, "server");

        serverValidator.validate(server, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("description");
        Assertions.assertThat(errors.getFieldError("description").getDefaultMessage()).isEqualTo("The description of this server is too long");
    }


}
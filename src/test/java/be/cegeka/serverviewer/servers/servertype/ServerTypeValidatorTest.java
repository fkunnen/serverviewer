package be.cegeka.serverviewer.servers.servertype;

import org.assertj.core.api.Assertions;
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

public class ServerTypeValidatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private ServerTypeValidator serverTypeValidator = new ServerTypeValidator();

    @Mock
    private ServerTypeRepository serverTypeRepository;

    @Test
    public void testSupports_ServerTypeClassReturnsTrue(){
        boolean isAssignableFrom = serverTypeValidator.supports(ServerType.class);
        Assertions.assertThat(isAssignableFrom).isTrue();
    }

    @Test
    public void testValidate_isValid(){
        ServerType serverType = new ServerType("Hardware Server");
        serverType.setDescription("Hardware server");
        Errors errors = new BeanPropertyBindingResult(serverType, "serverType");

        when(serverTypeRepository.findByName(serverType.getName())).thenReturn(Collections.EMPTY_LIST);

        serverTypeValidator.validate(serverType, errors);

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void testValidate_ServerTypeNameIsMandatory(){
        ServerType serverType = new ServerType();
        serverType.setDescription("Hardware server");
        Errors errors = new BeanPropertyBindingResult(serverType, "serverType");

        when(serverTypeRepository.findByName(serverType.getName())).thenReturn(Collections.EMPTY_LIST);

        serverTypeValidator.validate(serverType, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("Server type name is mandatory");
    }

    @Test
    public void testValidate_ServerTypeAlreadyExists(){
        ServerType serverType = new ServerType("Hardware Server");
        serverType.setDescription("Hardware server");
        Errors errors = new BeanPropertyBindingResult(serverType, "serverType");

        List<ServerType> currentServerTypes = Arrays.asList(serverType);
        when(serverTypeRepository.findByName(serverType.getName())).thenReturn(currentServerTypes);

        serverTypeValidator.validate(serverType, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("A server type with name 'Hardware Server' already exists");
    }

    @Test
    public void testValidate_ServerTypeNameTooLong(){
        ServerType serverType = new ServerType("The name for this server type is more than 45 characters, the maximum size for a server type name");
        serverType.setDescription("Hardware server");
        Errors errors = new BeanPropertyBindingResult(serverType, "serverType");

        when(serverTypeRepository.findByName(serverType.getName())).thenReturn(Collections.EMPTY_LIST);

        serverTypeValidator.validate(serverType, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("The name of this server type is too long");
    }

    @Test
    public void testValidate_ServerTypeDescriptionTooLong(){
        ServerType serverType = new ServerType("Hardware Server");
        serverType.setDescription("This server type can't be described in less than one hundred characters, the maximum size for a server type description");
        Errors errors = new BeanPropertyBindingResult(serverType, "serverType");

        when(serverTypeRepository.findByName(serverType.getName())).thenReturn(Collections.EMPTY_LIST);

        serverTypeValidator.validate(serverType, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("description");
        Assertions.assertThat(errors.getFieldError("description").getDefaultMessage()).isEqualTo("The description of this server type is too long");
    }


}
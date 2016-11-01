package be.cegeka.serverviewer.servers;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

public class EnvironmentValidatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private EnvironmentValidator environmentValidator = new EnvironmentValidator();

    @Mock
    private EnvironmentRepository environmentRepository;

    @Test
    public void testSupports_EnvironmentClassReturnsTrue(){
        boolean isAssignableFrom = environmentValidator.supports(Environment.class);
        Assertions.assertThat(isAssignableFrom).isTrue();
    }

    @Test
    public void testValidate_isValid(){
        Environment environment = new Environment("TST");
        environment.setDescription("Test environment");
        Errors errors = new BeanPropertyBindingResult(environment, "environment");

        when(environmentRepository.findByName(environment.getName())).thenReturn(Collections.EMPTY_LIST);

        environmentValidator.validate(environment, errors);

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void testValidate_EnvironmentNameIsMandatory(){
        Environment environment = new Environment();
        environment.setDescription("Test environment");
        Errors errors = new BeanPropertyBindingResult(environment, "environment");

        when(environmentRepository.findByName(environment.getName())).thenReturn(Collections.EMPTY_LIST);

        environmentValidator.validate(environment, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("Environment name is mandatory");
    }

    @Test
    public void testValidate_EnvironmentAlreadyExists(){
        Environment environment = new Environment("TST");
        environment.setDescription("Test environment");
        Errors errors = new BeanPropertyBindingResult(environment, "environment");

        List<Environment> currentEnvironments = Arrays.asList(environment);
        when(environmentRepository.findByName(environment.getName())).thenReturn(currentEnvironments);

        environmentValidator.validate(environment, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("An environment with name 'TST' already exists");
    }

    @Test
    public void testValidate_EnvironmentNameTooLong(){
        Environment environment = new Environment("The name for this environment is more than 45 characters, the maximum size for an environment name");
        environment.setDescription("Test environment");
        Errors errors = new BeanPropertyBindingResult(environment, "environment");

        when(environmentRepository.findByName(environment.getName())).thenReturn(Collections.EMPTY_LIST);

        environmentValidator.validate(environment, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("The name of this environment is too long");
    }

    @Test
    public void testValidate_EnvironmentDescriptionTooLong(){
        Environment environment = new Environment("TST");
        environment.setDescription("This test environment can't be described in less than one hundred characters, the maximum size for an environment description");
        Errors errors = new BeanPropertyBindingResult(environment, "environment");

        when(environmentRepository.findByName(environment.getName())).thenReturn(Collections.EMPTY_LIST);

        environmentValidator.validate(environment, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("description");
        Assertions.assertThat(errors.getFieldError("description").getDefaultMessage()).isEqualTo("The description of this environment is too long");
    }


}
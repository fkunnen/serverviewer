package be.cegeka.serverviewer.applications;

import org.assertj.core.api.Assertions;
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

public class ApplicationValidatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private ApplicationValidator applicationValidator = new ApplicationValidator();

    @Mock
    private ApplicationRepository applicationRepository;

    @Test
    public void testSupports_ApplicationClassReturnsTrue(){
        boolean isAssignableFrom = applicationValidator.supports(Application.class);
        Assertions.assertThat(isAssignableFrom).isTrue();
    }

    @Test
    public void testValidate_isValid(){
        Application application = new Application("B2B");
        application.setDescription("B2B applications");
        Errors errors = new BeanPropertyBindingResult(application, "applications");

        applicationValidator.validate(application, errors);

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void testValidate_ApplicationNameIsMandatory(){
        Application application = new Application();
        application.setDescription("B2B applications");
        Errors errors = new BeanPropertyBindingResult(application, "applications");

        applicationValidator.validate(application, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("Application name is mandatory");
    }

    @Test
    public void testValidate_ApplicationAlreadyExists(){
        Application application = new Application("B2B");
        application.setDescription("B2B applications");
        Errors errors = new BeanPropertyBindingResult(application, "applications");

        List<Application> currentApplications = Collections.singletonList(application);
        when(applicationRepository.findByName(application.getName())).thenReturn(currentApplications);

        applicationValidator.validate(application, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("An applications with name 'B2B' already exists");
    }

    @Test
    public void testValidate_ApplicationNameTooLong(){
        Application application = new Application("The name for this applications is more than 45 characters, the maximum size for a applications name");
        application.setDescription("B2B applications");
        Errors errors = new BeanPropertyBindingResult(application, "applications");

        applicationValidator.validate(application, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("The name of this applications is too long");
    }

    @Test
    public void testValidate_ApplicationDescriptionTooLong(){
        Application application = new Application("B2B");
        application.setDescription("This applications can't be described in less than one hundred characters, the maximum size for a applications description");
        Errors errors = new BeanPropertyBindingResult(application, "applications");

        applicationValidator.validate(application, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("description");
        Assertions.assertThat(errors.getFieldError("description").getDefaultMessage()).isEqualTo("The description of this applications is too long");
    }


}
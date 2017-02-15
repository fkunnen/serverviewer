package be.cegeka.serverviewer.deployments.deployment;


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

public class DeploymentValidatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private DeploymentValidator deploymentValidator = new DeploymentValidator();

    @Mock
    private DeploymentRepository deploymentRepository;

    private DeploymentTestBuilder deploymentTestBuilder;

    @Before
    public void setUp() throws Exception {
        deploymentTestBuilder = new DeploymentTestBuilder();
    }

    @Test
    public void testSupports_DeploymentClassReturnsTrue() {
        boolean isAssignableFrom = deploymentValidator.supports(Deployment.class);
        Assertions.assertThat(isAssignableFrom).isTrue();
    }

    @Test
    public void testValidate_isValid(){
        Deployment deployment = deploymentTestBuilder.build();
        Errors errors = new BeanPropertyBindingResult(deployment, "deployment");

        deploymentValidator.validate(deployment, errors);

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void testValidate_ApplicationIsMandatory(){
        Deployment deployment = deploymentTestBuilder.withApplication(null).build();
        Errors errors = new BeanPropertyBindingResult(deployment, "deployment");

        deploymentValidator.validate(deployment, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("application");
        Assertions.assertThat(errors.getFieldError("application").getDefaultMessage()).isEqualTo("Application is mandatory");
    }

    @Test
    public void testValidate_ServerIsMandatory(){
        Deployment deployment = deploymentTestBuilder.withServer(null).build();
        Errors errors = new BeanPropertyBindingResult(deployment, "deployment");

        deploymentValidator.validate(deployment, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("server");
        Assertions.assertThat(errors.getFieldError("server").getDefaultMessage()).isEqualTo("Server is mandatory");
    }

    @Test
    public void testValidate_ApplicationServerAlreadyExists(){
        Deployment deployment = deploymentTestBuilder.build();
        Errors errors = new BeanPropertyBindingResult(deployment, "deployment");

        List<Deployment> currentDeployments = Collections.singletonList(deployment);
        when(deploymentRepository.findByApplicationAndServer(deployment.getApplication(), deployment.getServer())).thenReturn(currentDeployments);

        deploymentValidator.validate(deployment, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("application");
        Assertions.assertThat(errors.getFieldError("application").getDefaultMessage()).isEqualTo("A deployment with application '" + deployment.getApplication().getName() + "' and server '" + deployment.getServer().getName() + "' already exists");
    }
}

package be.cegeka.serverviewer.deployments.middleware;

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

public class MiddlewareValidatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private MiddlewareValidator middlewareValidator = new MiddlewareValidator();

    @Mock
    private MiddlewareRepository middlewareRepository;

    @Test
    public void testSupports_MiddlewareClassReturnsTrue() {
        boolean isAssignableFrom = middlewareValidator.supports(Middleware.class);
        Assertions.assertThat(isAssignableFrom).isTrue();
    }

    @Test
    public void testValidate_isValid() {
        Middleware middleware = new Middleware("Weblogic 12c");
        middleware.setDescription("Oracle WebLogic Server 12.2.1.2");
        Errors errors = new BeanPropertyBindingResult(middleware, "middleware");

        middlewareValidator.validate(middleware, errors);

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void testValidate_MiddlewareNameIsMandatory() {
        Middleware middleware = new Middleware();
        middleware.setDescription("Oracle WebLogic Server 12.2.1.2");
        Errors errors = new BeanPropertyBindingResult(middleware, "middleware");

        middlewareValidator.validate(middleware, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("Middleware name is mandatory");
    }

    @Test
    public void testValidate_MiddlewareAlreadyExists() {
        Middleware middleware = new Middleware("Weblogic 12c");
        middleware.setDescription("Oracle WebLogic Server 12.2.1.2");
        Errors errors = new BeanPropertyBindingResult(middleware, "middleware");

        List<Middleware> currentMiddlewares = Collections.singletonList(middleware);
        when(middlewareRepository.findByName(middleware.getName())).thenReturn(currentMiddlewares);

        middlewareValidator.validate(middleware, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("A middleware with name 'Weblogic 12c' already exists");
    }

    @Test
    public void testValidate_MiddlewareNameTooLong() {
        Middleware middleware = new Middleware("The name for this middleware is more than 45 characters, the maximum size for a middleware name");
        middleware.setDescription("Oracle WebLogic Server 12.2.1.2");
        Errors errors = new BeanPropertyBindingResult(middleware, "middleware");

        middlewareValidator.validate(middleware, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("The name of this middleware is too long");
    }

    @Test
    public void testValidate_MiddlewareDescriptionTooLong() {
        Middleware middleware = new Middleware("Weblogic 12c");
        middleware.setDescription("This middleware can't be described in less than one hundred characters, the maximum size for a middleware description");
        Errors errors = new BeanPropertyBindingResult(middleware, "middleware");

        middlewareValidator.validate(middleware, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("description");
        Assertions.assertThat(errors.getFieldError("description").getDefaultMessage()).isEqualTo("The description of this middleware is too long");
    }


}


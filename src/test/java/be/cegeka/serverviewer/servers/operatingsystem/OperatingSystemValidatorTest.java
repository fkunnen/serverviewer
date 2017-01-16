package be.cegeka.serverviewer.servers.operatingsystem;

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

public class OperatingSystemValidatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private OperatingSystemValidator operatingSystemValidator = new OperatingSystemValidator();

    @Mock
    private OperatingSystemRepository operatingSystemRepository;

    @Test
    public void testSupports_OperatingSystemClassReturnsTrue(){
        boolean isAssignableFrom = operatingSystemValidator.supports(OperatingSystem.class);
        Assertions.assertThat(isAssignableFrom).isTrue();
    }

    @Test
    public void testValidate_isValid(){
        OperatingSystem operatingSystem = new OperatingSystem("RHEL 5 (32 bit)");
        operatingSystem.setDescription("Redhat Enterprise Linux 5 (32 bit)");
        Errors errors = new BeanPropertyBindingResult(operatingSystem, "operatingSystem");

        operatingSystemValidator.validate(operatingSystem, errors);

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void testValidate_OperatingSystemNameIsMandatory(){
        OperatingSystem operatingSystem = new OperatingSystem();
        operatingSystem.setDescription("Redhat Enterprise Linux 5 (32 bit)");
        Errors errors = new BeanPropertyBindingResult(operatingSystem, "operatingSystem");

        operatingSystemValidator.validate(operatingSystem, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("Operating system name is mandatory");
    }

    @Test
    public void testValidate_OperatingSystemAlreadyExists(){
        OperatingSystem operatingSystem = new OperatingSystem("RHEL 5 (32 bit)");
        operatingSystem.setDescription("Redhat Enterprise Linux 5 (32 bit)");
        Errors errors = new BeanPropertyBindingResult(operatingSystem, "operatingSystem");

        List<OperatingSystem> currentOperatingSystems = Collections.singletonList(operatingSystem);
        when(operatingSystemRepository.findByName(operatingSystem.getName())).thenReturn(currentOperatingSystems);

        operatingSystemValidator.validate(operatingSystem, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("An operating system with name 'RHEL 5 (32 bit)' already exists");
    }

    @Test
    public void testValidate_OperatingSystemNameTooLong(){
        OperatingSystem operatingSystem = new OperatingSystem("The name for this operating system is more than 45 characters, the maximum size for an operating system name");
        operatingSystem.setDescription("Redhat Enterprise Linux 5 (32 bit)");
        Errors errors = new BeanPropertyBindingResult(operatingSystem, "operatingSystem");

        operatingSystemValidator.validate(operatingSystem, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("The name of this operating system is too long");
    }

    @Test
    public void testValidate_OperatingSystemDescriptionTooLong(){
        OperatingSystem operatingSystem = new OperatingSystem("Leuven");
        operatingSystem.setDescription("This operating system can't be described in less than one hundred characters, the maximum size for an operating system description");
        Errors errors = new BeanPropertyBindingResult(operatingSystem, "operatingSystem");

        operatingSystemValidator.validate(operatingSystem, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("description");
        Assertions.assertThat(errors.getFieldError("description").getDefaultMessage()).isEqualTo("The description of this operating system is too long");
    }


}
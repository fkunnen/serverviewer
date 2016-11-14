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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

public class ClusterValidatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private ClusterValidator clusterValidator = new ClusterValidator();

    @Mock
    private ClusterRepository clusterRepository;

    @Test
    public void testSupports_ClusterClassReturnsTrue(){
        boolean isAssignableFrom = clusterValidator.supports(Cluster.class);
        Assertions.assertThat(isAssignableFrom).isTrue();
    }

    @Test
    public void testValidate_isValid(){
        Cluster cluster = new Cluster("B2B PRD Cluster");
        cluster.setDescription("B2B production cluster");
        Errors errors = new BeanPropertyBindingResult(cluster, "cluster");

        when(clusterRepository.findByName(cluster.getName())).thenReturn(Collections.EMPTY_LIST);

        clusterValidator.validate(cluster, errors);

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void testValidate_ClusterNameIsMandatory(){
        Cluster cluster = new Cluster();
        cluster.setDescription("B2B production cluster");
        Errors errors = new BeanPropertyBindingResult(cluster, "cluster");

        when(clusterRepository.findByName(cluster.getName())).thenReturn(Collections.EMPTY_LIST);

        clusterValidator.validate(cluster, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("Cluster name is mandatory");
    }

    @Test
    public void testValidate_ClusterAlreadyExists(){
        Cluster cluster = new Cluster("B2B PRD Cluster");
        cluster.setDescription("B2B production cluster");
        Errors errors = new BeanPropertyBindingResult(cluster, "cluster");

        List<Cluster> currentClusters = Arrays.asList(cluster);
        when(clusterRepository.findByName(cluster.getName())).thenReturn(currentClusters);

        clusterValidator.validate(cluster, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("A cluster with name 'B2B PRD Cluster' already exists");
    }

    @Test
    public void testValidate_ClusterNameTooLong(){
        Cluster cluster = new Cluster("The name for this cluster is more than 45 characters, the maximum size for a cluster name");
        cluster.setDescription("B2B production cluster");
        Errors errors = new BeanPropertyBindingResult(cluster, "cluster");

        when(clusterRepository.findByName(cluster.getName())).thenReturn(Collections.EMPTY_LIST);

        clusterValidator.validate(cluster, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("The name of this cluster is too long");
    }

    @Test
    public void testValidate_ClusterDescriptionTooLong(){
        Cluster cluster = new Cluster("B2B PRD Cluster");
        cluster.setDescription("This cluster can't be described in less than one hundred characters, the maximum size for a cluster description");
        Errors errors = new BeanPropertyBindingResult(cluster, "cluster");

        when(clusterRepository.findByName(cluster.getName())).thenReturn(Collections.EMPTY_LIST);

        clusterValidator.validate(cluster, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("description");
        Assertions.assertThat(errors.getFieldError("description").getDefaultMessage()).isEqualTo("The description of this cluster is too long");
    }


}
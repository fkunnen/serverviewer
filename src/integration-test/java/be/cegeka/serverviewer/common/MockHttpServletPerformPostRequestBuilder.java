package be.cegeka.serverviewer.common;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class MockHttpServletPerformPostRequestBuilder {

    public static MockHttpServletRequestBuilder postForm(String url, Object modelAttribute, String... propertyPaths) throws Exception {

            MockHttpServletRequestBuilder postFormRequest = post(url).characterEncoding("UTF-8").contentType(MediaType.APPLICATION_FORM_URLENCODED);

            for (String propertyPath : propertyPaths) {
                postFormRequest.param(propertyPath, BeanUtils.getProperty(modelAttribute, propertyPath));
            }

            return postFormRequest;
    }
}

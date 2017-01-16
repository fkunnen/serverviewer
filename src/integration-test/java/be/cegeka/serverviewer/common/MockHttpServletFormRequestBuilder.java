package be.cegeka.serverviewer.common;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.lang.reflect.InvocationTargetException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class MockHttpServletFormRequestBuilder {

    public static MockHttpServletRequestBuilder postForm(String url, Object modelAttribute, String... propertyPaths) throws Exception {

        return createMockHttpServletRequestBuilder(post(url), modelAttribute, propertyPaths);
    }

    public static MockHttpServletRequestBuilder putForm(String url, Object modelAttribute, String... propertyPaths) throws Exception {

        return createMockHttpServletRequestBuilder(put(url), modelAttribute, propertyPaths);
    }

    private static MockHttpServletRequestBuilder createMockHttpServletRequestBuilder(MockHttpServletRequestBuilder requestBuilder, Object modelAttribute, String[] propertyPaths) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        MockHttpServletRequestBuilder formRequest = requestBuilder.characterEncoding("UTF-8").contentType(MediaType.APPLICATION_FORM_URLENCODED);

        for (String propertyPath : propertyPaths) {
            formRequest.param(propertyPath, BeanUtils.getProperty(modelAttribute, propertyPath));
        }

        return formRequest;
    }
}

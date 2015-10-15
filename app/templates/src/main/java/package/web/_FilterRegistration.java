package <%=packageNameGenerated%>.web;

import <%=packageNameGenerated%>.web.filter.LifeTimeSessionAndRequestTokenFilter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by rparry on 10/13/15.
 */
@Component
public class FilterRegistration {

    @Bean
    public FilterRegistrationBean lifeTimeSessionAndRequestTokenFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(lifeTimeSessionAndRequestTokenFilter());
        registration.addUrlPatterns("/api/*");
        registration.setName("lifeTimeSessionAndRequestTokenFilter");
        return registration;
    }

    @Bean(name = "lifeTimeSessionAndRequestTokenFilter")
    public LifeTimeSessionAndRequestTokenFilter lifeTimeSessionAndRequestTokenFilter() {
        return new LifeTimeSessionAndRequestTokenFilter();
    }
}

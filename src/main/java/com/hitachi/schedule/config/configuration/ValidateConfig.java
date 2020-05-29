package com.hitachi.schedule.config.configuration;


import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Configuration
public class ValidateConfig {
    @Bean
    public Validator validator() {
        PlatformResourceBundleLocator prbl = new PlatformResourceBundleLocator("public/ValidationMessages");
        ResourceBundleMessageInterpolator messageSource = new ResourceBundleMessageInterpolator(prbl);

        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .messageInterpolator(messageSource)
                .failFast(true)
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        return validator;
    }
}
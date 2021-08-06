package com.kep.iruma.common.config

import com.kep.iruma.common.filter.LoggingFilter
import com.kep.iruma.common.interceptor.BaseInterceptor
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    @Bean
    fun getFilterRegistrationBean(): FilterRegistrationBean<LoggingFilter>? {
        val filterRegistrationBean: FilterRegistrationBean<LoggingFilter> = FilterRegistrationBean(LoggingFilter())
        filterRegistrationBean.setName("loggingFilter")
        filterRegistrationBean.addUrlPatterns("/api/*")
        return filterRegistrationBean
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(BaseInterceptor()).addPathPatterns("/**")
    }
}

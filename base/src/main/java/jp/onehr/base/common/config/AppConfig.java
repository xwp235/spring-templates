package jp.onehr.base.common.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jp.onehr.base.common.config.properties.SystemProperties;
import jp.onehr.base.common.config.serializer.LocalDateTimeSerializer;
import jp.onehr.base.common.config.serializer.ZonedDateTimeSerializer;
import jp.onehr.base.common.constants.AppConstants;
import jp.onehr.base.common.filters.EntryPointFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Configuration
@EnableConfigurationProperties(SystemProperties.class)
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 所有api接口都应用此配置
        registry.addMapping("/**")
                // 允许所有来源
                .allowedOriginPatterns(CorsConfiguration.ALL)
                // 允许所有header
                .allowedHeaders(CorsConfiguration.ALL)
                // 允许所有请求方式(GET,POST,...)
                .allowedMethods(CorsConfiguration.ALL)
                .exposedHeaders(AppConstants.X_AUTHENTICATE, AppConstants.X_REQUESTED_ID)
                // 允许请求带上cookie
                .allowCredentials(true)
                // 一小时内不再需要预检（发送OPTIONS请求）
                .maxAge(3600);
    }

    @Bean
    com.fasterxml.jackson.databind.Module simpleModule(ZonedDateTimeSerializer zonedDateTimeSerializer,
                                                       LocalDateTimeSerializer localDateTimeSerializer) {
        var simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(Double.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Double.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(Float.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Float.TYPE, ToStringSerializer.instance);

        simpleModule.addSerializer(ZonedDateTime.class, zonedDateTimeSerializer);
        simpleModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        return simpleModule;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        // 允许特定的域名（可以使用 "*" 允许所有域名）
        configuration.setAllowedOrigins(List.of(CorsConfiguration.ALL));
        // 允许的 HTTP 方法，如 GET、POST 等
        configuration.setAllowedMethods(List.of(CorsConfiguration.ALL));
        // 允许的请求头（可以使用 "*" 允许所有头部）
        configuration.setAllowedHeaders(List.of(CorsConfiguration.ALL));
        // 是否允许发送 Cookie
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of(AppConstants.X_AUTHENTICATE,AppConstants.X_REQUESTED_ID));
        // 一小时内不再需要预检（发送OPTIONS请求）
        configuration.setMaxAge(3600L);
        // 配置路径匹配策略
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }

    @Bean
    FilterRegistrationBean<EntryPointFilter> entryPointFilter() {
        var registrationBean = new FilterRegistrationBean<EntryPointFilter>();
        registrationBean.setFilter(new EntryPointFilter());
        registrationBean.setUrlPatterns(List.of("/*"));
        registrationBean.setEnabled(true);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

// 有ExceptionHandler注解时重写此方法无效
//    @Override
//    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
//        resolvers.remove(resolvers.size()-1);
//        resolvers.add(new DefaultHandlerExceptionResolver(){
//            @Override
//            public ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex,
//                                                                 HttpServletRequest request,
//                                                                 HttpServletResponse response,
//                                                                 Object handler) throws IOException {
//                return new ModelAndView((model, request1, response1) -> ServletUtil.getWriter(response1).println(ex.getMessage()));
//            }
//        });
//    }

}

package jp.onehr.base.common.service;

import jp.onehr.base.common.req.RouteMappingReq;
import jp.onehr.base.common.utils.ReflectUtil;
import jp.onehr.base.common.utils.SpringUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RegistryMappingService {

    private static final Map<Object, Method[]> METHOD_CACHE = new ConcurrentHashMap<>();

    public RegistryMappingService(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;

        var routeMapping = new RouteMappingReq();
        routeMapping.setSpringBean(false);
        routeMapping.setInstanceName("jp.onehr.base.common.service.UserHandler");
        routeMapping.setRequestMethod(HttpMethod.GET);
        routeMapping.setPath("/user/list");
        routeMapping.setMethodName("list");
        var rs = register(routeMapping);
    }

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public boolean register(RouteMappingReq routeMapping) {
        RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
        options.setPatternParser(new PathPatternParser());
        var info = RequestMappingInfo.paths(routeMapping.getPath())
                .methods(RequestMethod.resolve(routeMapping.getRequestMethod()))
                .options(options)
                .build();
        Object handler;
        var instName = routeMapping.getInstanceName();
        if (routeMapping.isSpringBean()) {
            handler = SpringUtil.getBean(instName);
        } else {
            handler = ReflectUtil.newInstance(instName);
        }
        var methods = METHOD_CACHE.computeIfAbsent(handler, key -> {
                    System.out.println(key);
            System.out.println(handler.getClass());
            return ReflectionUtils.getAllDeclaredMethods(handler.getClass());
        });

        var method = Arrays.stream(methods)
                .filter(m->m.getName().equals(routeMapping.getMethodName()))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(method)) {
            return false;
        }
        requestMappingHandlerMapping.registerMapping(info,handler,method);
        return true;
    }

}

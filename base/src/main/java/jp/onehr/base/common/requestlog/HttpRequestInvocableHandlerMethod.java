package jp.onehr.base.common.requestlog;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.util.Arrays;

public class HttpRequestInvocableHandlerMethod extends ServletInvocableHandlerMethod {

    public HttpRequestInvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }

    @Override
    public Object doInvoke(Object... args) throws Exception {
        var attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes() ;
        // 获取当前的请求URI
        var url = attr.getRequest().getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        System.err.printf("-------------请求URI：%s-------------%n", url) ;
        System.out.printf("请求参数: %s%n", Arrays.toString(args)) ;
        // 该方法才是真正调用目标Controller中的方法
        Object ret = super.doInvoke(args);
        System.out.printf("响应结果: %s%n", ret);
        System.err.println("-------------end-------------");
        return ret;
    }

}

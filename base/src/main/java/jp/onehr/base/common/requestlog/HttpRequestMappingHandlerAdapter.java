package jp.onehr.base.common.requestlog;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

public class HttpRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {

    @Override
    public ServletInvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
        return new HttpRequestInvocableHandlerMethod(handlerMethod) ;
    }

}

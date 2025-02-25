package jp.onehr.base.common.requestlog;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Component
public class HttpRequestWebMvcRegistrations implements WebMvcRegistrations {

    @Override
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        return new HttpRequestMappingHandlerAdapter();
    }

}

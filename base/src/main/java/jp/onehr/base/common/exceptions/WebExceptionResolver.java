package jp.onehr.base.common.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import java.util.List;

@Component
public class WebExceptionResolver extends AbstractHandlerExceptionResolver {

    private final List<IExceptionHandler> handlers;

    public WebExceptionResolver(List<IExceptionHandler> handlers) {
        setOrder(Ordered.HIGHEST_PRECEDENCE);
        // 最高优先级
        this.handlers = handlers;
        System.err.println("handlers size:" + handlers.size());
    }

    @Override
    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        for (var exceptionHandler : handlers) {
            if (exceptionHandler.support(ex)) {
                return exceptionHandler.handlerException(request, response, handler, ex);
            }
        }
        // 返回null则交由下一个解析器进行处理
        return null;
    }

}

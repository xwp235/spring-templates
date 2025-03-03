package jp.onehr.base.common.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ModelAndView;

public interface IExceptionHandler extends Ordered {

    /**
     * 是否支持的异常
     */
    boolean support(Exception e);

    /**
     * 异常处理方法
     */
    ModelAndView handlerException(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception e);

    @Override
    default int getOrder() {
        return 0;
    }

}

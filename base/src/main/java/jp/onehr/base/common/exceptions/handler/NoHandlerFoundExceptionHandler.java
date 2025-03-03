package jp.onehr.base.common.exceptions.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.onehr.base.common.exceptions.resolver.AbstractExceptionHandler;
import jp.onehr.base.common.resp.JsonResp;
import jp.onehr.base.common.utils.ServletUtil;
import jp.onehr.base.common.utils.SpringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.NoHandlerFoundException;

@Component
public class NoHandlerFoundExceptionHandler extends AbstractExceptionHandler {

    @Override
    public boolean support(Exception e) {
        return NoHandlerFoundException.class.isAssignableFrom(e.getClass());
    }

    public NoHandlerFoundExceptionHandler() {
        super(HttpStatus.OK);
    }

    @Override
    public Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        var message = SpringUtil.getMessage("notFound");
        var httpStatus = HttpStatus.NOT_FOUND;
        if (ServletUtil.isAjaxRequest(request)) {
            return JsonResp
                    .error(message)
                    .setCode(httpStatus.value());
        } else {
            return redirect404View();
        }
    }

}

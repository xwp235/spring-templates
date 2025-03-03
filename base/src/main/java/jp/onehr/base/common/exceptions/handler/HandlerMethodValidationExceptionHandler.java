package jp.onehr.base.common.exceptions.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.onehr.base.common.exceptions.resolver.AbstractExceptionHandler;
import jp.onehr.base.common.resp.JsonResp;
import jp.onehr.base.common.utils.JsonUtil;
import jp.onehr.base.common.utils.ServletUtil;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;

// 触发handler条件 	@Validated + 约束
// 适用参数验证注解 @RequestParam、@PathVariable、@RequestHeader
@Component
public class HandlerMethodValidationExceptionHandler extends AbstractExceptionHandler {

    @Override
    public boolean support(Exception e) {
        return HandlerMethodValidationException.class.isAssignableFrom(e.getClass());
    }

    public HandlerMethodValidationExceptionHandler() {
        super(HttpStatus.OK);
    }

    @Override
    public Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        var errors = new HashMap<String, Object>();
        var httpStatus = HttpStatus.BAD_REQUEST;
        var e = (HandlerMethodValidationException) ex;
        var allErrors = e.getAllErrors();
        for (var error : allErrors) {
            var args = error.getArguments();
            var arg0 = (DefaultMessageSourceResolvable) args[0];
            errors.put(arg0.getDefaultMessage(), error.getDefaultMessage());
        }
        if (ServletUtil.isAjaxRequest(request)) {
            return JsonResp
                    .error(JsonUtil.obj2Json(errors))
                    .setCode(httpStatus.value());
        } else {
            return errorView(errors, httpStatus);
        }
    }

}

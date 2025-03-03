package jp.onehr.base.common.exceptions.handler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.onehr.base.common.exceptions.resolver.AbstractExceptionHandler;
import jp.onehr.base.common.resp.JsonResp;
import jp.onehr.base.common.utils.ServletUtil;
import jp.onehr.base.common.utils.SpringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;

// 触发条件 @Valid校验失败
// 适用数验证注解 @RequestBody、@ModelAttribute
@Component
public class MethodArgumentNotValidExceptionHandler extends AbstractExceptionHandler {

    @Override
    public boolean support(Exception e) {
        return MethodArgumentNotValidException.class.isAssignableFrom(e.getClass());
    }

    public MethodArgumentNotValidExceptionHandler() {
        super(HttpStatus.OK);
    }

    @Override
    protected Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        var errors = new HashMap<String, Object>();
        var e = (MethodArgumentNotValidException) ex;
        var allErrors = e.getAllErrors();
        var httpStatus = HttpStatus.BAD_REQUEST;
        for (var error : allErrors) {
            if (error instanceof FieldError fieldError) {
                errors.put("field-" + fieldError.getField(), fieldError.getDefaultMessage());
            } else if (error instanceof ObjectError objectError) {
                errors.put("validator-" + objectError.getObjectName(), error.getDefaultMessage());
            }
        }
        if (ServletUtil.isAjaxRequest(request)) {
            return JsonResp
                    .error(SpringUtil.getMessage("error_invalid_request_parameters"))
                    .setData(errors)
                    .setCode(httpStatus.value());
        } else {
            return errorView(errors, httpStatus);
        }
    }

}

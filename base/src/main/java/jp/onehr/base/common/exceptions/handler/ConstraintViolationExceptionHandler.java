package jp.onehr.base.common.exceptions.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jp.onehr.base.common.exceptions.AbstractExceptionHandler;
import jp.onehr.base.common.resp.JsonResp;
import jp.onehr.base.common.utils.ServletUtil;
import jp.onehr.base.common.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;

// service类上加@Validated，方法参数上加验证注解时抛出
@Component
public class ConstraintViolationExceptionHandler extends AbstractExceptionHandler {

    @Override
    public boolean support(Exception e) {
        return ConstraintViolationException.class.isAssignableFrom(e.getClass());
    }

    public ConstraintViolationExceptionHandler() {
        super(HttpStatus.OK);
    }

    @Override
    public Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        var httpStatus = HttpStatus.BAD_REQUEST;
        var errors = new HashMap<String, Object>();
        var e = (ConstraintViolationException) ex;
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            var className = constraintViolation.getRootBeanClass().getName();
            var fieldPath = constraintViolation.getPropertyPath();
            var message = constraintViolation.getMessage();
            errors.put(className + '@' + fieldPath, message);
        }

        var message = SpringUtil.getMessage("error_invalid_server_request_parameters");
        printDevLog(message, e);
        if (ServletUtil.isAjaxRequest(request)) {
            var resp = JsonResp
                    .error(message)
                    .setCode(httpStatus.value());
            var activeProfile = SpringUtil.getActiveProfile();
            var isDev = StringUtils.equalsIgnoreCase("dev", activeProfile);
            if (isDev) {
                resp.setData(errors);
            }
            return ResponseEntity.status(HttpStatus.OK.value()).body(resp);
        } else {
            return errorView(errors, httpStatus);
        }
    }

}

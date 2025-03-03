package jp.onehr.base.common.exceptions.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.onehr.base.common.enums.ExceptionLevel;
import jp.onehr.base.common.exceptions.AbstractExceptionHandler;
import jp.onehr.base.common.exceptions.AppException;
import jp.onehr.base.common.resp.JsonResp;
import jp.onehr.base.common.utils.SpringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AppExceptionHandler extends AbstractExceptionHandler {

    @Override
    public boolean support(Exception e) {
        return AppException.class.isAssignableFrom(e.getClass());
    }

    public AppExceptionHandler() {
        super(HttpStatus.OK);
    }

    @Override
    public Object doHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        var e = (AppException) ex;
        var rootErrorInfo = getRootErrorInfo(e);
        var message = e.getMessage();
        var level = e.getLevel();
        if (Objects.isNull(rootErrorInfo)) {
            if (level == ExceptionLevel.ERROR || level == ExceptionLevel.FATAL) {
                message = SpringUtil.getMessage("appErrorOccurred");
            } else {
                message = SpringUtil.getMessage("appWarningOccurred");
            }
        } else {
            message = rootErrorInfo.toString();
        }
        if (e.shouldLog()) {
            if (level == ExceptionLevel.ERROR || level == ExceptionLevel.FATAL) {
                logger.error(message, e);
            } else if (level == ExceptionLevel.WARN) {
                logger.warn(message, e);
            } else {
                logger.info(message, e);
            }
        }
        return JsonResp.error(message).setCode(e.getCode())
                .setExceptionTypeWithTraceId(level);
    }

}

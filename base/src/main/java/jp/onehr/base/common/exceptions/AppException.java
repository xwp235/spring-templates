package jp.onehr.base.common.exceptions;

import jp.onehr.base.common.constants.StatusCodeConstants;
import jp.onehr.base.common.enums.ExceptionLevel;
import jp.onehr.base.common.utils.SpringUtil;

import java.io.Serial;

public class AppException extends StatefulException {

    private final ExceptionLevel level;

    @Serial
    private static final long serialVersionUID = 6057602589533840890L;

    public AppException(Throwable throwable) {
        super(StatusCodeConstants.SERVER_INTERNAL_ERROR,
                SpringUtil.getMessage("serverInternalError"), throwable, false, false);
        level = ExceptionLevel.ERROR;
    }

    public AppException(int code, ExceptionLevel level, Throwable throwable) {
        super(code, SpringUtil.getMessage("serverInternalError"), throwable, false, false);
        this.level = level;
    }

    public AppException(int code, ExceptionLevel level, String message, Throwable throwable) {
        super(code,message, throwable, false, false);
        this.level = level;
    }

    public AppException(ExceptionLevel level, String message, Throwable throwable) {
        super(StatusCodeConstants.SERVER_INTERNAL_ERROR, message, throwable, false, false);
        this.level = level;
    }

    public AppException(String message, ExceptionLevel level, Throwable throwable) {
        super(StatusCodeConstants.SERVER_INTERNAL_ERROR, message, throwable, false, false);
        this.level = level;
    }

    public AppException(String message, Throwable throwable) {
        super(StatusCodeConstants.SERVER_INTERNAL_ERROR, message, throwable, false, false);
        this.level = ExceptionLevel.ERROR;
    }

    public AppException(int code, String message, Throwable throwable) {
        super(code, message, throwable, false, false);
        this.level = ExceptionLevel.ERROR;
    }

    public ExceptionLevel getLevel() {
        return this.level;
    }

}

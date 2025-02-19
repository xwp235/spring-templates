package jp.onehr.base.common.exceptions;

import jp.onehr.base.common.constants.StatusCodeConstants;
import jp.onehr.base.common.enums.ExceptionLevel;
import jp.onehr.base.common.utils.SpringUtil;

import java.io.Serial;

public class AppException extends StatefulException {

    private final ExceptionLevel level;
    private final boolean shouldLog;

    @Serial
    private static final long serialVersionUID = 6057602589533840890L;

    public AppException(boolean shouldLog) {
        super(StatusCodeConstants.SERVER_INTERNAL_ERROR,
                SpringUtil.getMessage("serverInternalError"),null, false, false);
        this.level = ExceptionLevel.ERROR;
        this.shouldLog = shouldLog;
    }

    public AppException(boolean shouldLog, Throwable throwable) {
        super(StatusCodeConstants.SERVER_INTERNAL_ERROR,
                SpringUtil.getMessage("serverInternalError"), throwable, false, false);
        this.level = ExceptionLevel.ERROR;
        this.shouldLog = shouldLog;
    }

    public AppException(int code, boolean shouldLog,ExceptionLevel level, Throwable throwable) {
        super(code, SpringUtil.getMessage("serverInternalError"), throwable, false, false);
        this.level = level;
        this.shouldLog = shouldLog;
    }

    public AppException(int code, boolean shouldLog, ExceptionLevel level) {
        super(code, SpringUtil.getMessage("serverInternalError"), null, false, false);
        this.level = level;
        this.shouldLog = shouldLog;
    }

    public AppException(int code, String message, boolean shouldLog, ExceptionLevel level, Throwable throwable) {
        super(code,message, throwable, false, false);
        this.level = level;
        this.shouldLog = shouldLog;
    }

    public AppException(int code,String message, boolean shouldLog, ExceptionLevel level) {
        super(code,message, null, false, false);
        this.level = level;
        this.shouldLog = shouldLog;
    }

    public AppException(String message,boolean shouldLog, ExceptionLevel level) {
        super(StatusCodeConstants.SERVER_INTERNAL_ERROR, message, null, false, false);
        this.level = level;
        this.shouldLog = shouldLog;
    }

    public AppException(String message, boolean shouldLog, ExceptionLevel level, Throwable throwable) {
        super(StatusCodeConstants.SERVER_INTERNAL_ERROR, message, throwable, false, false);
        this.level = level;
        this.shouldLog = shouldLog;
    }


    public AppException(String message, boolean shouldLog, Throwable throwable) {
        super(StatusCodeConstants.SERVER_INTERNAL_ERROR, message, throwable, false, false);
        this.level = ExceptionLevel.ERROR;
        this.shouldLog = shouldLog;
    }

    public AppException(int code, String message, Throwable throwable) {
        super(code, message, throwable, false, false);
        this.level = ExceptionLevel.ERROR;
        this.shouldLog = true;
    }

    public AppException(int code, String message, boolean shouldLog) {
        super(code, message, null, false, false);
        this.shouldLog = shouldLog;
        if(shouldLog) {
            this.level = ExceptionLevel.ERROR;
        } else {
            this.level = ExceptionLevel.INFO;
        }
    }

    public ExceptionLevel getLevel() {
        return this.level;
    }

    public boolean shouldLog() {
        return this.shouldLog;
    }

}

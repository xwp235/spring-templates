package jp.onehr.base.common.exceptions.resolver;

import jp.onehr.base.common.constants.StatusCodeConstants;
import jp.onehr.base.common.enums.ExceptionLevel;
import jp.onehr.base.common.utils.SpringUtil;

import java.io.Serial;

public class AppException extends RuntimeException {

    private final int code;
    private final ExceptionLevel level;
    private final boolean shouldLog;

    @Serial
    private static final long serialVersionUID = 6057602589533840890L;

    public AppException(boolean shouldLog) {
        super(SpringUtil.getMessage("serverInternalError"), null, false, false);
        this.code = StatusCodeConstants.SERVER_INTERNAL_ERROR;
        this.level = ExceptionLevel.ERROR;
        this.shouldLog = shouldLog;
    }

    public AppException(boolean shouldLog, Throwable throwable) {
        super(SpringUtil.getMessage("serverInternalError"), throwable, false, false);
        this.code = StatusCodeConstants.SERVER_INTERNAL_ERROR;
        this.level = ExceptionLevel.ERROR;
        this.shouldLog = shouldLog;
    }

    public AppException(int code, boolean shouldLog, ExceptionLevel level, Throwable throwable) {
        super(SpringUtil.getMessage("serverInternalError"), throwable, false, false);
        this.code = code;
        this.level = level;
        this.shouldLog = shouldLog;
    }

    public AppException(int code, boolean shouldLog, ExceptionLevel level) {
        super(SpringUtil.getMessage("serverInternalError"), null, false, false);
        this.code = code;
        this.level = level;
        this.shouldLog = shouldLog;
    }

    public AppException(int code, String message, boolean shouldLog, ExceptionLevel level, Throwable throwable) {
        super(message, throwable, false, false);
        this.code = code;
        this.level = level;
        this.shouldLog = shouldLog;
    }

    public AppException(int code, String message, boolean shouldLog, ExceptionLevel level) {
        super(message, null, false, false);
        this.code = code;
        this.level = level;
        this.shouldLog = shouldLog;
    }

    public AppException(String message, boolean shouldLog, ExceptionLevel level) {
        super(message, null, false, false);
        this.code = StatusCodeConstants.SERVER_INTERNAL_ERROR;
        this.level = level;
        this.shouldLog = shouldLog;
    }

    public AppException(String message, boolean shouldLog, ExceptionLevel level, Throwable throwable) {
        super(message, throwable, false, false);
        this.code = StatusCodeConstants.SERVER_INTERNAL_ERROR;
        this.level = level;
        this.shouldLog = shouldLog;
    }


    public AppException(String message, boolean shouldLog, Throwable throwable) {
        super(message, throwable, false, false);
        this.code = StatusCodeConstants.SERVER_INTERNAL_ERROR;
        this.level = ExceptionLevel.ERROR;
        this.shouldLog = shouldLog;
    }

    public AppException(int code, String message, Throwable throwable) {
        super(message, throwable, false, false);
        this.code = code;
        this.level = ExceptionLevel.ERROR;
        this.shouldLog = true;
    }

    public AppException(int code, String message, boolean shouldLog) {
        super(message, null, false, false);
        this.code = code;
        this.shouldLog = shouldLog;
        if (shouldLog) {
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

    public int getCode() {
        return this.code;
    }

}

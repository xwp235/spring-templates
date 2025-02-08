package jp.onehr.base.common.exceptions;

import jp.onehr.base.common.enums.ExceptionLevel;

import java.io.Serial;

public class AppException extends StatefulException {

    private final ExceptionLevel level;

    @Serial
    private static final long serialVersionUID = 6057602589533840890L;

    public AppException(Throwable throwable) {
        super(500, "Server error", throwable, false, false);
        level = ExceptionLevel.ERROR;
    }

    public AppException(int code, ExceptionLevel level, Throwable throwable) {
        super(code, "Server error", throwable, false, false);
        this.level = level;
    }

    public AppException(int code, ExceptionLevel level, String message, Throwable throwable) {
        super(code,message, throwable, false, false);
        this.level = level;
    }

    public AppException(ExceptionLevel level, String message, Throwable throwable) {
        super(500,message, throwable, false, false);
        this.level = level;
    }

    public AppException(String message, ExceptionLevel level, Throwable throwable) {
        super(500,message, throwable, false, false);
        this.level = level;
    }

    public ExceptionLevel getLevel() {
        return this.level;
    }
    
}

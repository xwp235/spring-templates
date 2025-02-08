package jp.onehr.base.common.exceptions;

import java.io.Serial;

public class StatefulException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6057602589533840889L;

    // 异常状态码
    private int code;

    public StatefulException() {
    }

    public StatefulException(String msg) {
        super(msg);
    }

    public StatefulException(String messageTemplate, Object... params) {
        super(String.format(messageTemplate, params));
    }

    public StatefulException(Throwable throwable) {
        super(throwable);
    }

    public StatefulException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public StatefulException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public StatefulException(int code, String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public StatefulException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public StatefulException(int code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public StatefulException(int code, String msg, Throwable throwable) {
        super(msg, throwable);
        this.code = code;
    }

    /**
     * @return 获得异常状态码
     */
    public int getCode() {
        return this.code;
    }

}

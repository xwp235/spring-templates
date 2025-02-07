package jp.onehr.base.common.exceptions;

import jp.onehr.base.common.utils.ExceptionUtil;

import java.io.Serial;
import java.util.Objects;

/**
 * IO运行时异常，常用于对IOException的包装
 *
 */
public class IORuntimeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8247610319171014183L;

    public IORuntimeException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public IORuntimeException(String message) {
        super(message);
    }

    public IORuntimeException(String messageTemplate, Object... params) {
        super(String.format(messageTemplate, params));
    }

    public IORuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public IORuntimeException(Throwable throwable, String messageTemplate, Object... params) {
        super(String.format(messageTemplate, params), throwable);
    }

    /**
     * 导致这个异常的异常是否是指定类型的异常
     *
     * @param clazz 异常类
     * @return 是否为指定类型异常
     */
    public boolean causeInstanceOf(Class<? extends Throwable> clazz) {
        var cause = this.getCause();
        return Objects.nonNull(clazz) && clazz.isInstance(cause);
    }

}

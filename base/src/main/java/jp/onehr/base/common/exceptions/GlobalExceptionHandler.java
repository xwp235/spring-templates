package jp.onehr.base.common.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jp.onehr.base.InitTemplateApplication;
import jp.onehr.base.common.enums.ExceptionLevel;
import jp.onehr.base.common.resp.JsonResp;
import jp.onehr.base.common.utils.ServletUtil;
import jp.onehr.base.common.utils.SpringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

/**
 *
 * status：服务器生成的 HTTP 状态代码。
 * type：一个用于标识问题类型及如何解决该问题的URL。默认值为"about:blank"。
 * title：问题的简短描述。
 * detail：针对此次问题详细解释。
 * instance：发生问题的服务的 URL。默认值是当前请求的 URL。
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object httpRequestMethodNotSupportedException(HttpServletRequest request) {
        var message = SpringUtil.getMessage("request_method_not_supported");
        var httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        if (ServletUtil.isAjaxRequest(request)) {
            return ResponseEntity.status(HttpStatus.OK.value())
                    .body(JsonResp
                            .error(message)
                            .setCode(httpStatus.value())
                    );
        } else {
            return errorView("error", message,httpStatus);
        }
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Object noHandlerFoundException(HttpServletRequest request) {
        var httpStatus = HttpStatus.NOT_FOUND;
        var message = SpringUtil.getMessage("notFound");
        if (ServletUtil.isAjaxRequest(request)) {
            return ResponseEntity.status(HttpStatus.OK.value())
                    .body(JsonResp
                            .error(message)
                            .setCode(httpStatus.value())
                    );
        } else {
            return errorView("404", message, httpStatus);
        }
    }

    /**
     * 业务异常统一处理
     */
    @ExceptionHandler(AppException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public JsonResp appException(AppException e) {
        var rootErrorInfo = getRootErrorInfo(e);
        var message = e.getMessage();
        var level = e.getLevel();
        if (Objects.isNull(rootErrorInfo)) {
            if (level==ExceptionLevel.ERROR||level==ExceptionLevel.FATAL) {
                message = SpringUtil.getMessage("appErrorOccurred");
            } else {
                message = SpringUtil.getMessage("appWarningOccurred");
            }
        }
        if (level==ExceptionLevel.ERROR||level==ExceptionLevel.FATAL) {
            logger.error(message, e);
        } else if (level==ExceptionLevel.WARN) {
            logger.warn(message, e);
        } else {
            logger.info(message, e);
        }
        return JsonResp.error(message).setCode(e.getCode())
                .setExceptionTypeWithTraceId(level);
    }

//    @Override
//    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleMissingPathVariable(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleMissingServletRequestParameter(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleMissingServletRequestPart(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleServletRequestBindingException(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleMethodArgumentNotValid(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleHandlerMethodValidationException(ex, headers, status, request);
//    }



//    @Override
//    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleNoResourceFoundException(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleAsyncRequestTimeoutException(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleErrorResponseException(ErrorResponseException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleErrorResponseException(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleMaxUploadSizeExceededException(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleConversionNotSupported(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleTypeMismatch(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleHttpMessageNotReadable(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        return super.handleHttpMessageNotWritable(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMethodValidationException(MethodValidationException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleMethodValidationException(ex, headers, status, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleAsyncRequestNotUsableException(AsyncRequestNotUsableException ex, WebRequest request) {
//        return super.handleAsyncRequestNotUsableException(ex, request);
//    }

//    @Override
//    public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
//        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
//    }

    private ModelAndView errorView(String viewName, String message,HttpStatus status) {
        var mav = new ModelAndView(viewName);
        mav.setStatus(status);
        mav.addObject("message", message);
        mav.addObject("status",status.value());
        return mav;
    }

    private RootErrorInfo getRootErrorInfo(Throwable e) {
        var rootCause = ExceptionUtils.getRootCause(e);
        if (Objects.isNull(rootCause)) {
            return null;
        }
        var stackTrace = rootCause.getStackTrace();
        if (ArrayUtils.isEmpty(stackTrace)) {
            return null;
        }
        var rootPackage = ClassUtils.getPackageName(InitTemplateApplication.class);
        return getRootInfoDetail(stackTrace, rootPackage);
    }

    private RootErrorInfo getRootInfoDetail(StackTraceElement[] stackTrace, String rootPackage) {
        var info = stackTrace[0];
        for (var stackTraceElement : stackTrace) {
            var stackElStr = stackTraceElement.toString();
            if (stackElStr.contains(rootPackage)) {
                info = stackTraceElement;
                break;
            }
        }
        return new RootErrorInfo(info.getLineNumber(),info.getClassName(),info.getMethodName());
    }

}

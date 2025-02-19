package jp.onehr.base.common.exceptions;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.onehr.base.InitTemplateApplication;
import jp.onehr.base.common.enums.ExceptionLevel;
import jp.onehr.base.common.resp.JsonResp;
import jp.onehr.base.common.utils.JsonUtil;
import jp.onehr.base.common.utils.ServletUtil;
import jp.onehr.base.common.utils.SpringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
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
    public void noHandlerFoundException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var httpStatus = HttpStatus.NOT_FOUND;
        var message = SpringUtil.getMessage("notFound");
        if (ServletUtil.isAjaxRequest(request)) {
            var json = JsonUtil.obj2Json(JsonResp
                    .error(message)
                    .setCode(httpStatus.value()));
            ServletUtil.write(response,json, MediaType.APPLICATION_JSON);
        } else {
            response.setStatus(httpStatus.value());
            response.sendRedirect("/404");
        }
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Object noResourceFoundException(HttpServletRequest request) {
        var httpStatus = HttpStatus.NOT_FOUND;
        var message = SpringUtil.getMessage("error_resource_not_found");
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

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Object httpMediaTypeNotSupportedException(HttpServletRequest request) {
        var httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        var message = SpringUtil.getMessage("error_request_media_type_not_supported");
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

    @ExceptionHandler(Exception.class)
    public Object exception(Exception e,HttpServletRequest request) {
        var message = SpringUtil.getMessage("serverInternalError");
        var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        logger.error(message, e);
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

    @ExceptionHandler({MissingServletRequestPartException.class,MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class, MissingPathVariableException.class,
            ServletRequestBindingException.class})
    public Object clientRequestException(HttpServletRequest request) {
        var message = SpringUtil.getMessage("error_invalid_request_parameters");
        var httpStatus = HttpStatus.BAD_REQUEST;
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

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public Object httpMediaTypeNotAcceptableException(HttpServletRequest request) {
        var message = SpringUtil.getMessage("error_http_media_type_not_acceptable");
        var httpStatus = HttpStatus.NOT_ACCEPTABLE;
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

    // 触发条件 	@Validated + 约束
    // 适用参数验证注解 @RequestParam、@PathVariable、@RequestHeader
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Object handlerMethodValidationException(HttpServletRequest request, HandlerMethodValidationException e) {
        e.visitResults(new HandlerMethodValidationException.Visitor() {
            @Override
            public void requestHeader(RequestHeader requestHeader, ParameterValidationResult result) {
                // 处理 @RequestHeader 参数的验证错误
                // ...
            }

            @Override
            public void requestParam(@Nullable RequestParam requestParam, ParameterValidationResult result) {
                // 处理 @RequestParam 参数的验证错误
                // ...
            }

            @Override
            public void modelAttribute(@Nullable ModelAttribute modelAttribute, ParameterErrors errors) {
                // 处理 @ModelAttribute 参数的验证错误
                // ...
            }

            @Override
            public void other(ParameterValidationResult result) {
                // 处理其他类型的验证错误
                // ...
            }
        });
        return null;
    }

    // 触发条件 @Valid校验失败
    // 适用数验证注解 @RequestBody、@ModelAttribute
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Object methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        e.getBindingResult().getFieldErrors().forEach(error ->
                System.out.println(error.getField()+"->"+error.getDefaultMessage())
        );
        return null;
    }

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

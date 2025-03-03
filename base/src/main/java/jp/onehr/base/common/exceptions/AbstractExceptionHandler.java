package jp.onehr.base.common.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.onehr.base.InitTemplateApplication;
import jp.onehr.base.common.resp.JsonResp;
import jp.onehr.base.common.utils.SpringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractExceptionHandler implements IExceptionHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final HttpStatus httpStatus;
    @Resource
    protected ObjectMapper objectMapper;
    protected MappingJackson2JsonView mappingJackson2JsonView;

    public AbstractExceptionHandler(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public boolean support(Exception e) {
        return false;
    }

    @Override
    public final ModelAndView handlerException(HttpServletRequest request,
                                               HttpServletResponse response,
                                               Object handler,
                                               Exception e) {
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            response.setStatus(httpStatus.value());
            var responseData = this.doHandler(request, response, handler, e);
            ModelAndView modelAndView;
            if (responseData instanceof JsonResp r) {
                if (Objects.isNull(mappingJackson2JsonView)) {
                    mappingJackson2JsonView = new MappingJackson2JsonView(objectMapper);
                }
                modelAndView = new ModelAndView(mappingJackson2JsonView);
                modelAndView.addAllObjects(r);
                return modelAndView;
            } else {
                return (ModelAndView) responseData;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    protected abstract Object doHandler(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Object handler, Exception e);

    protected ModelAndView errorView(String message, HttpStatus status) {
        var mav = new ModelAndView("error");
        mav.setStatus(status);
        mav.addObject("message", message);
        mav.addObject("status", status.value());
        return mav;
    }

    protected ModelAndView errorView(Map<String, Object> errors, HttpStatus status) {
        var mav = new ModelAndView("error");
        mav.setStatus(status);
        mav.addObject("errors", errors);
        mav.addObject("status", status.value());
        return mav;
    }

    protected ModelAndView redirectView(String path) {
        var mav = new ModelAndView();
        mav.setViewName("redirect:" + path);
        return mav;
    }

    protected RootErrorInfo getRootErrorInfo(Throwable e) {
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
        return new RootErrorInfo(info.getLineNumber(), info.getClassName(), info.getMethodName());
    }

    protected void printDevLog(String message, Exception e) {
        var activeProfile = SpringUtil.getActiveProfile();
        var isDev = StringUtils.equalsIgnoreCase("dev", activeProfile);
        if (isDev) {
            logger.error(message, e);
        }
    }

}

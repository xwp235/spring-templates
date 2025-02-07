package jp.onehr.base.common.utils;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jp.onehr.base.common.constants.StringConstants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ServletUtil {

    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_TRACE = "TRACE";

    // --------------------------------------------------------- getParam start

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象{@link ServletRequest}
     * @return Map
     */
    public static Map<String, String[]> getParams(ServletRequest request) {
        final Map<String, String[]> map = request.getParameterMap();
        return Collections.unmodifiableMap(map);
    }

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象{@link ServletRequest}
     * @return Map
     */
    public static Map<String, String> getParamMap(ServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : getParams(request).entrySet()) {
            params.put(entry.getKey(), ArrayUtil.join(entry.getValue(), StringConstants.COMMA));
        }
        return params;
    }

    /**
     * 获取请求体<br>
     * 调用该方法后，getParam方法将失效
     *
     * @param request {@link ServletRequest}
     * @return 获得请求体
     */
    public static String getBody(ServletRequest request) {
        try {
            return IOUtils.toString(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
//            throw new IORuntimeException(e);
        }
    }

    /**
     * 获取请求体byte[]<br>
     * 调用该方法后，getParam方法将失效
     *
     * @param request {@link ServletRequest}
     * @return 获得请求体byte[]
     */
    public static byte[] getBodyBytes(ServletRequest request) {
        try (var in = request.getInputStream()){
            return IOUtils.readFully(in, in.available());
        } catch (IOException e) {
//            throw new IORuntimeException(e);
        }
    }
    // --------------------------------------------------------- getParam end


    public static boolean isAjaxRequest(HttpServletRequest request) {
        var acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        var contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        var contentLength = request.getContentLength();
        var acceptJson = Objects.nonNull(acceptHeader) && acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE);
        var contentJson = Objects.nonNull(contentType) && StringUtils.equals(contentType, MediaType.APPLICATION_JSON_VALUE) && contentLength > 0;
        return acceptJson || contentJson;
    }

    /**
     * 是否为GET请求
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 是否为GET请求
     */
    public static boolean isGetMethod(HttpServletRequest request) {
        return METHOD_GET.equalsIgnoreCase(request.getMethod());
    }

    /**
     * 是否为POST请求
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 是否为POST请求
     */
    public static boolean isPostMethod(HttpServletRequest request) {
        return METHOD_POST.equalsIgnoreCase(request.getMethod());
    }

}

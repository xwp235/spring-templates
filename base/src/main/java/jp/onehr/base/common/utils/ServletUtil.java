package jp.onehr.base.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Objects;

public class ServletUtil {

    public static boolean isAjaxRequest(HttpServletRequest request) {
        var acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        var contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        var contentLength = request.getContentLength();
        var acceptJson = Objects.nonNull(acceptHeader) && acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE);
        var contentJson = Objects.nonNull(contentType) && StringUtils.equals(contentType, MediaType.APPLICATION_JSON_VALUE) && contentLength > 0;
        return acceptJson || contentJson;
    }

}

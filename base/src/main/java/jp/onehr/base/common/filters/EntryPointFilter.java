package jp.onehr.base.common.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.onehr.base.common.config.ReusableHttpServletRequestWrapper;
import jp.onehr.base.common.constants.AppConstants;
import jp.onehr.base.common.utils.ServletUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class EntryPointFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 生成一个唯一的 LOG_ID
        String logId = request.getHeader(AppConstants.X_REQUESTED_ID);
        if (StringUtils.isBlank(logId)) {
            logId = UUID.randomUUID().toString();
        }
        try {
            MDC.put(AppConstants.MDC.LOG_ID, logId);
            if (!ServletUtil.isGetMethod(request) && ServletUtil.isAjaxRequest(request)) {
                filterChain.doFilter(new ReusableHttpServletRequestWrapper(request), response);
            } else {
                filterChain.doFilter(request, response);
            }
        } finally {
            MDC.remove(AppConstants.MDC.LOG_ID);
        }
    }

}

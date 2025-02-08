package jp.onehr.base.common.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

import java.util.Objects;

//@Component
public class RequestLogListener implements ApplicationListener<ServletRequestHandledEvent> {

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        var error = event.getFailureCause();
        if (Objects.nonNull(error)){
            System.err.printf("error: %s\n", error.getMessage());
        }
        System.out.println("==================>");
        System.err.printf("客户端地址:%s\n请求url：%s\n请求方式:%s\n请求耗时:%d毫秒\n",
                event.getClientAddress(),
                event.getRequestUrl(),
                event.getMethod(),
                event.getProcessingTimeMillis());
        System.err.println("<==================");
    }

}

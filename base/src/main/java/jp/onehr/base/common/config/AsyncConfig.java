package jp.onehr.base.common.config;

import jp.onehr.base.common.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(SpringUtil.class);

    @Value("${spring.threads.virtual.enabled:off}")
    private boolean enableVirtualThread;

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (e, method, params) -> logger.error("Async task execute error occurred!", e);
    }

    @Bean
    Executor asyncExecutor(TaskExecutionProperties taskExecutionProperties) {
        var executor = new ThreadPoolTaskExecutor();
        var poolConfig = taskExecutionProperties.getPool();
        var shutdownConfig = taskExecutionProperties.getShutdown();
        executor.setCorePoolSize(poolConfig.getCoreSize());
        executor.setMaxPoolSize(poolConfig.getMaxSize());
        executor.setQueueCapacity(poolConfig.getQueueCapacity());
        executor.setKeepAliveSeconds(Math.toIntExact(poolConfig.getKeepAlive().toSeconds()));
        executor.setThreadNamePrefix(taskExecutionProperties.getThreadNamePrefix());
        executor.setWaitForTasksToCompleteOnShutdown(shutdownConfig.isAwaitTermination());
        executor.setAwaitTerminationSeconds(Math.toIntExact(shutdownConfig.getAwaitTerminationPeriod().toSeconds()));
        executor.setVirtualThreads(enableVirtualThread);
        executor.setRejectedExecutionHandler(
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        executor.setTaskDecorator(runnable -> {
            var contextMap = MDC.getCopyOfContextMap();
            return () -> {
                try {
                    if (Objects.nonNull(contextMap)) {
                        MDC.setContextMap(contextMap);
                    } else {
                        MDC.clear();
                    }
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        });
        executor.initialize();
        return executor;
    }

}

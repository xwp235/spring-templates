package jp.onehr.base.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jp.onehr.base.common.annotation.OperationAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Service
@Validated
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Async
    public void getList() {
        logger.info("userService getList executed!");
        int i = 1 / 0;
    }

    public CompletableFuture<String> longRunningTask() {
        try {
//            Thread.sleep(5000);
            // 模拟耗时任务（5 秒）
            int a = 1 / 0;
        } catch (Exception e) {
//            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
        return CompletableFuture.completedFuture("Success");
    }

    public String getUserById(@NotNull Long id) { // 如果 id 传 null，就会抛出异常
        return "User-" + id;
    }

    public void createProduct(@Valid Product product) {
        System.out.println("Product created: " + product);
    }

    @OperationAudit(
            code = "A1",
            description = "参数：#a1",
            preState = "#a1"
    )
    public String testAudit(MultipartFile file, String a1) {
        return a1 + "-->";
    }
}

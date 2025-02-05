package jp.onehr.base;

import jp.onehr.base.common.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InitTemplateApplication {

    public static void main(String[] args) {
        SpringUtil.loadEnvFile(args);
        SpringApplication.run(InitTemplateApplication.class, args);
    }

}

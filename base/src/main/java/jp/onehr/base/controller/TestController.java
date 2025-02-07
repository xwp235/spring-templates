package jp.onehr.base.controller;

import jp.onehr.base.common.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("1")
    public Map<String,Object> lang1() {
        return Map.of("msg1", SpringUtil.getMessage("objSerialize2JsonFailed"));
    }

    @GetMapping("2")
    public String restartApp() {
        SpringUtil.restartApp();
        return "Restart app";
    }

    @GetMapping("3")
    public Map<String,Object> test3() {
        logger.info("test3 executed! logId is printed");
        var ld1 = LocalDateTime.now();
        var d1 = new Date();
        var zd1 = ZonedDateTime.now();
        return Map.of(
                "d1",d1,
                "zd1",zd1,
                "localDateTime1",ld1,"double1", 1.24,
                "float1",1.1f,"long1",100L);
    }

}

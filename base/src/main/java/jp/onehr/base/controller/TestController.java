package jp.onehr.base.controller;

import jp.onehr.base.common.utils.SpringUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("1")
    public Map<String,Object> lang1() {
        return Map.of("msg1", SpringUtil.getMessage("objSerialize2JsonFailed"));
    }

}

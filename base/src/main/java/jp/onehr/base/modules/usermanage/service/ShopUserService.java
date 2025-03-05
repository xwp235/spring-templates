package jp.onehr.base.modules.usermanage.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class ShopUserService {

    private final CommonService commonService;

    public ShopUserService(CommonService commonService) {
        this.commonService = commonService;
    }

    public void queryUser() {
        var timeout = new Random().nextInt(1000);
        try {
            TimeUnit.MICROSECONDS.sleep(timeout);
            System.out.println("查询用户信息");
            this.commonService.calc();
        } catch (InterruptedException e) {

        }
    }

}

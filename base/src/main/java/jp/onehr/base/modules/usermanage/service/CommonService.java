package jp.onehr.base.modules.usermanage.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CommonService {

    public void calc() {
        try {
            TimeUnit.MICROSECONDS.sleep(20);
        } catch (InterruptedException e) {
        }
    }

}

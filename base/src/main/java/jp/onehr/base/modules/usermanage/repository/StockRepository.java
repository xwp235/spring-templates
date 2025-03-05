package jp.onehr.base.modules.usermanage.repository;

import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class StockRepository {

    public void queryStock() {
        try {
            TimeUnit.MICROSECONDS.sleep(10);
        } catch (InterruptedException e) {

        }
    }

}

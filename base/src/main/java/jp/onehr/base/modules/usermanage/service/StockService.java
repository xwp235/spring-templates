package jp.onehr.base.modules.usermanage.service;

import jp.onehr.base.modules.usermanage.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void queryStock() {
        var timeout = new Random().nextInt(1000);
        try {
            TimeUnit.MICROSECONDS.sleep(timeout);
            this.stockRepository.queryStock();
            System.out.println("查询库存");
        } catch (InterruptedException e) {

        }
    }

}

package jp.onehr.base.modules.usermanage.service;

import jp.onehr.base.modules.usermanage.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {

    private final StockRepository stockRepository;

    public OrderService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void queryOrder() {
        var timeout = new Random().nextInt(1000);
        try {
            TimeUnit.MICROSECONDS.sleep(timeout);
            System.out.println("查询库存");
            this.stockRepository.queryStock();
        } catch (InterruptedException e) {

        }
    }
}

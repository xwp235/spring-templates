package jp.onehr.base.common.service;

import jp.onehr.base.common.entity.AuditRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class AuditService {

    private volatile long lastFlushTime = System.currentTimeMillis();
    private static final long MAX_FLUSH_INTERVAL = 3000;
    private static final int MAX_RETRY_ATTEMPTS = 3; // 最大重试次数
    private static final BlockingQueue<AuditRecord> auditQueue = new LinkedBlockingQueue<>(1000);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final AtomicBoolean running = new AtomicBoolean(true); // 用于停止线程

    public AuditService() {
        startConsumer();
    }

    public void record(AuditRecord auditRecord) {
        if (!auditQueue.offer(auditRecord)) {
            System.err.printf("Audit queue is full, record will be discarded: %s%n", auditRecord);
        }
    }

    private void startConsumer() {
        executor.execute(() -> {
            var buffer = new ArrayList<AuditRecord>(100);
            var retryCount = 0; // 失败重试计数器

            while (running.get() && !Thread.interrupted()) {
                try {
                    var record = auditQueue.poll(MAX_FLUSH_INTERVAL, TimeUnit.MILLISECONDS);
                    if (Objects.nonNull(record)) {
                        buffer.add(record);
                    }
                    var reachBatchSize = buffer.size() >= 100;
                    var reachTimeLimit = System.currentTimeMillis() - lastFlushTime > MAX_FLUSH_INTERVAL;
                    if (reachBatchSize || reachTimeLimit) {
                        if (!buffer.isEmpty()) {
                            saveAuditRecords(buffer); // 模拟保存
                            buffer.clear();
                            lastFlushTime = System.currentTimeMillis();
                        }
                    }

                    // 成功后重置重试计数器
                    retryCount = 0;

                } catch (Exception e) {
                    retryCount++;
                    System.err.printf("Audit consumer failed, retrying (%d/%d)... %s%n",
                            retryCount, MAX_RETRY_ATTEMPTS, e.getMessage());
                    if (retryCount >= MAX_RETRY_ATTEMPTS) {
                        System.err.println("Audit consumer failed too many times, stopping service.");
                        running.set(false);
                        break;
                    }
                    try {
                        Thread.sleep(1000); // 等待 1 秒后再试
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            // 退出前保存剩余数据
            if (!buffer.isEmpty()) {
                saveAuditRecords(buffer);
            }
        });
    }

    private void saveAuditRecords(List<AuditRecord> records) {
        // TODO: 批量保存审计数据到数据库
        System.out.println("Saving " + records.size() + " audit records...");
    }

}

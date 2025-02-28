package jp.onehr.base.modules.usermanage.service;

import jp.onehr.base.common.service.UserHandler;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.Map;

// 事务操作演示示例
// JdbcClient使用： https://mp.weixin.qq.com/s/J0XEX6aATJMamdu6m6A3gw
// Spring提供的批量数据库操作： https://mp.weixin.qq.com/s/SLylgJumY_HGurZDiDVEvw
@Service
public class UserManagerService {

    private final DataSourceTransactionManager transactionManager;
    private final TransactionTemplate transactionTemplate;
    private final JdbcClient jdbcClient;

    public UserManagerService(DataSourceTransactionManager transactionManager, JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
        this.transactionManager = transactionManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        this.transactionTemplate.setTimeout(TransactionDefinition.TIMEOUT_DEFAULT);
    }

    public void save(UserHandler.User user) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    String sql = "update t_users t set t.username = :un, t.password = :pwd where t.id = :id";
                    Map<String, Object> params = Map.of("un", "guest007", "pwd", "99999", "id", "666");
                    int ret = jdbcClient.sql(sql).params(params).update();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 当发生异常后设置为回滚
                    status.setRollbackOnly();
                }
            }
        });
    }

    public void saveWithResult(UserHandler.User user) {
        transactionTemplate.execute(
                (TransactionCallback<Object>) status -> {
                    try {
                        String sql = "update t_users t set t.username = :un, t.password = :pwd where t.id = :id";
                        Map<String, Object> params = Map.of("un", "guest007", "pwd", "99999", "id", "666");
                        int ret = jdbcClient.sql(sql).params(params).update();
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 当发生异常后设置为回滚
                        status.setRollbackOnly();
                    }
                    return "success";
                }
        );
    }

    public void save() {
        UserHandler.User person = new UserHandler.User(1, "jack", 30, "男", new Date());

        var definition = new DefaultTransactionDefinition();
        definition.setName("CustomTx");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        definition.setReadOnly(false);
        definition.setTimeout(2);

        var transactionStatus = transactionManager.getTransaction(definition);
        try {
            String sql = "update t_users t set t.username = :un, t.password = :pwd where t.id = :id";
            Map<String, Object> params = Map.of("un", "guest007", "pwd", "99999", "id", "666");
            int ret = jdbcClient.sql(sql).params(params).update();
            // 制造异常
            System.out.println(1 / 0);

            // 提交事务
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            e.printStackTrace();
            // 回滚事务
            transactionManager.rollback(transactionStatus);
        }
    }
}

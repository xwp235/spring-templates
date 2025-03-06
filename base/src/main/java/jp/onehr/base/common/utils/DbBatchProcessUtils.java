package jp.onehr.base.common.utils;

import jp.onehr.base.modules.usermanage.service.Person;
import org.apache.commons.collections4.ListUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DbBatchProcessUtils {

    private static final Executor defaultExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public static void batchQueryIn(List<?> ids, int batchSize) {
        var jdbcClient = SpringUtil.getBean(JdbcClient.class);
        var partitions = ListUtils.partition(ids, batchSize);
        var result = partitions.stream().map(chunk -> CompletableFuture.supplyAsync(() -> {
                    List<Map<String, Object>> results = jdbcClient.sql(
                                    "SELECT * FROM t_test_person WHERE id IN (:ids)")
                            .param("ids", chunk, Types.INTEGER)
                            .query()
                            .listOfRows();
                    return results;
                }, defaultExecutor))
                .toList()
                .stream()
                .map(CompletableFuture::join)
                .filter(list -> list != null && !list.isEmpty())
                .toList();
//        System.out.println("--->batch result:");
//        System.out.println(result);
    }

    public static void batchInsert() {
        var persons = generatePersons(3000);
        var jdbcTemplate = SpringUtil.getBean(JdbcTemplate.class);
        int[][] ret = jdbcTemplate.batchUpdate("insert into t_test_person (name,age,birthday) values (?, ?,?)",
                persons, 50, (PreparedStatement ps, Person p) -> {
                    ps.setString(1, p.getName());
                    ps.setInt(2, p.getAge());
                    ps.setDate(3, new java.sql.Date(p.getBirthday().getTime()));
                });
        System.out.println("ret:" + ret.length);
    }

    public static List<Person> generatePersons(int count) {
        Random random = new Random();

        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> {
                    Person person = new Person();
                    person.setName("Person_" + i);
                    person.setAge(random.nextInt(51) + 18); // 生成 18-68 之间的年龄
                    person.setBirthday(new Date(
                            (long) (random.nextDouble() * (System.currentTimeMillis() - 946684800000L) + 946684800000L)
                    )); // 生成 2000 年到当前时间的随机生日
                    return person;
                })
                .collect(Collectors.toList());
    }

    public void batchUpdate() {
        var jdbcTemplate = SpringUtil.getBean(JdbcTemplate.class);
        String sql = "UPDATE t_test_person SET age = ? WHERE id = ?";

        List<Object[]> batchArgs = new ArrayList<>();

        // 假设我们有 2500 条数据要批量更新
        for (int i = 1; i <= 2500; i++) {
            batchArgs.add(new Object[]{randomAge(), i}); // 更新每个人的年龄
        }

        // 使用 JdbcTemplate 的 batchUpdate 方法执行批量更新
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    private int randomAge() {
        // 返回一个随机的年龄
        return (int) (Math.random() * 50 + 18); // 18 - 68
    }

}

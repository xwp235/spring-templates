package jp.onehr.base.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OperationAudit {

    // 操作类型编号
    String code();

    // 操作描述（支持SpEL表达式）
    String description() default "";

    // 操作人表达式
    String operator() default "@auditOperator.getName";

    // 前置状态提取表达式（操作前）
    String preState() default "";

    // 后置状态提取表达式（操作后）
    String postState() default "#result";

}

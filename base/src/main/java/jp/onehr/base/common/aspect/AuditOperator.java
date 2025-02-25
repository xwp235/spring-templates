package jp.onehr.base.common.aspect;

import org.springframework.stereotype.Component;

@Component
public class AuditOperator {

    public String getName() {
        return "audit-operation";
    }

}

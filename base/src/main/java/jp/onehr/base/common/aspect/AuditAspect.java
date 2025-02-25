package jp.onehr.base.common.aspect;

import jp.onehr.base.common.annotation.OperationAudit;
import jp.onehr.base.common.entity.AuditRecord;
import jp.onehr.base.common.service.AuditService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuditAspect implements BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(AuditAspect.class);
    private BeanFactory beanFactory;
    private final AuditService auditService;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Around("@annotation(auditAnnotation)")
    public Object auditOperation(ProceedingJoinPoint joinPoint, OperationAudit auditAnnotation) throws Throwable {
        var method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        var args = joinPoint.getArgs();
        var ctx = createEvaluationContext(method, args);
        var record = prepareAuditRecord(auditAnnotation, ctx);
        // 记录前置状态
        record.setPreState(evaluateExpression(auditAnnotation.preState(), ctx));
        try {
            var result = joinPoint.proceed();
            // 记录执行结果及操作后的状态
            ctx.setVariable("result", result);
            record.setPostState(evaluateExpression(auditAnnotation.postState(), ctx));
            record.setSuccess(true);
            return result;
        } catch (Exception e) {
            record.setSuccess(false);
            record.setErrorMessage(e.getMessage());
            throw e;
        } finally {
            // 将日志信息添加到队列中
            auditService.record(record);
        }
    }

    private AuditRecord prepareAuditRecord(OperationAudit annotation, EvaluationContext ctx) {
        var record = new AuditRecord();
        record.setOperationCode(annotation.code());
        record.setDescription(evaluateExpression(annotation.description(), ctx));
        record.setOperator(evaluateExpression(annotation.operator(), ctx));
        return record;
    }

    private EvaluationContext createEvaluationContext(Method method, Object[] args) {
        var ctx = new MethodBasedEvaluationContext(null, method, args, parameterNameDiscoverer);
        ctx.setBeanResolver(new BeanFactoryResolver(this.beanFactory));
        return ctx;
    }

    private String evaluateExpression(String expr, EvaluationContext ctx) {
        try {
            return parser.parseExpression(expr).getValue(ctx, String.class);
        } catch (Exception e) {
            logger.warn("SpEL evaluation failed:{}", expr, e);
            return "";
        }
    }

}

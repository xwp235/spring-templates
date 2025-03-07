package jp.onehr.base.common.interceptor;

import jp.onehr.base.common.entity.MethodNode;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ScopedValueExecutionTimeInterceptor implements MethodInterceptor {

    private static final ScopedValue<Deque<MethodNode>> METHOD_NODE_STACK = ScopedValue.newInstance();
    private static final ScopedValue<MethodNode> ROOT_METHOD_NODE = ScopedValue.newInstance();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        var method = invocation.getMethod();
        var methodName = method.getDeclaringClass() + "#" + method.getName();
        var currentMethodNode = new MethodNode(methodName);

        Deque<MethodNode> stack = METHOD_NODE_STACK.isBound() ? METHOD_NODE_STACK.get() : new ArrayDeque<>();

        if (stack.isEmpty()) {
            // 创建新的作用域
            return ScopedValue.where(ROOT_METHOD_NODE, currentMethodNode)
                    .where(METHOD_NODE_STACK, stack)
                    .call(() -> {
                        try {
                            return execute(invocation, currentMethodNode, stack);
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    });
        } else {
            // 继续当前作用域
            stack.peek().add(currentMethodNode);
            stack.push(currentMethodNode);
            return execute(invocation, currentMethodNode, stack);
        }
    }

    private Object execute(MethodInvocation invocation, MethodNode currentMethodNode, Deque<MethodNode> stack) throws Throwable {
        var startTime = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            var endTime = System.currentTimeMillis();
            currentMethodNode.setDuration(endTime - startTime);
            stack.pop();

            if (stack.isEmpty()) {
                System.out.println("Call Tree (Total: " + ROOT_METHOD_NODE.get().getDuration() + "ms)");
                printTree(ROOT_METHOD_NODE.get());
            }
        }
    }

    private void printTree(MethodNode rootMethodNode) {
        var sb = new StringBuilder();
        buildRecursiveMethodTree(rootMethodNode, sb, 0, new ArrayList<>());
        System.out.println(sb);
    }

    private void buildRecursiveMethodTree(MethodNode node, StringBuilder sb, int level, List<Boolean> lastFlags) {
        var prefix = new StringBuilder();
        for (var i = 0; i < level - 1; i++) {
            prefix.append(lastFlags.get(i) ? "    " : " |    ");
        }
        if (level > 0) {
            prefix.append(lastFlags.get(level - 1) ? " └── " : " ├── ");
        }
        sb.append(String.format("%s%s - %dms %s%n", prefix, node.getMethodName(), node.getDuration(), node.getDuration() > 1 ? "⚠️" : ""));
        var children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            var newFlags = new ArrayList<>(lastFlags);
            newFlags.add(i == children.size() - 1);
            buildRecursiveMethodTree(children.get(i), sb, level + 1, newFlags);
        }
    }

}

package jp.onehr.base.common.interceptor;

import jp.onehr.base.common.entity.MethodNode;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ExecutionTimeInterceptor implements MethodInterceptor {

    private static final InheritableThreadLocal<Deque<MethodNode>> METHOD_NODE_STACK = new InheritableThreadLocal<>() {
        @Override
        public Deque<MethodNode> initialValue() {
            return new ArrayDeque<>();
        }
    };
    private static final InheritableThreadLocal<MethodNode> ROOT_METHOD_NODE = new InheritableThreadLocal<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        var method = invocation.getMethod();
        var methodName = method.getDeclaringClass() + "#" + method.getName();
        var currentMethodNode = new MethodNode(methodName);
        var stack = METHOD_NODE_STACK.get();
        if (stack.isEmpty()) {
            ROOT_METHOD_NODE.set(currentMethodNode);
        } else {
            stack.peek().add(currentMethodNode);
        }
        stack.push(currentMethodNode);
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
                ROOT_METHOD_NODE.remove();
                METHOD_NODE_STACK.remove();
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
        for (int i = 0; i < level - 1; i++) {
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

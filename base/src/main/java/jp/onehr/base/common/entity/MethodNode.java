package jp.onehr.base.common.entity;

import java.util.ArrayList;
import java.util.List;

public class MethodNode {

    private final String methodName;
    private long duration;
    private List<MethodNode> children = new ArrayList<>();

    public MethodNode(String methodName) {
        this.methodName = methodName;
    }

    public void add(MethodNode node) {
        this.children.add(node);
    }

    public String getMethodName() {
        return methodName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<MethodNode> getChildren() {
        return children;
    }

    public void setChildren(List<MethodNode> children) {
        this.children = children;
    }
    
}

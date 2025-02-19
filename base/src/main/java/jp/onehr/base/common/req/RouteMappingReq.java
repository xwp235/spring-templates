package jp.onehr.base.common.req;

import org.springframework.http.HttpMethod;

public class RouteMappingReq {

    private String path;
    private HttpMethod requestMethod;
    private String instanceName;
    private String methodName;
    private boolean springBean;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(HttpMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isSpringBean() {
        return this.springBean;
    }

    public void setSpringBean(boolean springBean) {
        this.springBean = springBean;
    }

}

package jp.onehr.base.common.exceptions;

import jp.onehr.base.common.utils.SpringUtil;

public class RootErrorInfo {

    public RootErrorInfo() {}

    public RootErrorInfo(Integer lineNumber,String className,String methodName) {
        this.lineNumber = lineNumber;
        this.className = className;
        this.methodName = methodName;
    }

    private Integer lineNumber;
    private String className;
    private String methodName;

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return SpringUtil.getMessage("error_root_error_position", className,methodName,lineNumber+"");
    }
}

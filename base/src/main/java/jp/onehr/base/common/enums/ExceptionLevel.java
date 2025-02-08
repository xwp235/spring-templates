package jp.onehr.base.common.enums;

public enum ExceptionLevel {
    INFO("INFO"), WARN("WARN"), ERROR("ERROR"), FATAL("FATAL");

    ExceptionLevel(String level) {
        this.level = level;
    }

    private final String level;

    public String getLevel() {
        return this.level;
    }

}

package jp.onehr.base.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config")
public class SystemProperties {

    private String defaultTimezone;
    private String defaultDatetimePattern;
    private LoggingProperties logging;

    public void setDefaultTimezone(String defaultTimezone) {
        this.defaultTimezone = defaultTimezone;
    }

    public String getDefaultTimezone() {
        return this.defaultTimezone;
    }

    public void setDefaultDatetimePattern(String defaultDatetimePattern) {
        this.defaultDatetimePattern = defaultDatetimePattern;
    }

    public String getDefaultDatetimePattern() {
        return defaultDatetimePattern;
    }

    public LoggingProperties getLogging() {
        return logging;
    }

    public void setLogging(LoggingProperties logging) {
        this.logging = logging;
    }

    public static class LoggingProperties {

        private boolean structured;

        public boolean isStructured() {
            return structured;
        }

        public void setStructured(boolean structured) {
            this.structured = structured;
        }
    }

}

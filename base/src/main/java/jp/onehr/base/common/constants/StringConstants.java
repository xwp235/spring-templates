package jp.onehr.base.common.constants;

/**
 * 常用字符串常量定义
 * @see CharConstants

 */
public interface StringConstants {


    /**
     * 字符串常量：空格符 {@code " "}
     */
    String SPACE = " ";
    /**
     * 字符串常量：{@code "null"} <br>
     * 注意：{@code "null" != null}
     */
    String NULL = "null";
    /**
     * 字符串常量：空字符串 {@code ""}
     */
    String EMPTY = "";
    /**
     * 字符常量：空格符 {@code ' '}
     */
    char C_SPACE = CharConstants.SPACE;

    /**
     * 字符常量：制表符 {@code '\t'}
     */
    char C_TAB = CharConstants.TAB;

    /**
     * 字符常量：点 {@code '.'}
     */
    char C_DOT = CharConstants.DOT;

    /**
     * 字符常量：斜杠 {@code '/'}
     */
    char C_SLASH = CharConstants.SLASH;

    /**
     * 字符常量：反斜杠 {@code '\\'}
     */
    char C_BACKSLASH = CharConstants.BACKSLASH;

    /**
     * 字符常量：回车符 {@code '\r'}
     */
    char C_CR = CharConstants.CR;

    /**
     * 字符常量：换行符 {@code '\n'}
     */
    char C_LF = CharConstants.LF;

    /**
     * 字符常量：下划线 {@code '_'}
     */
    char C_UNDERLINE = CharConstants.UNDERLINE;

    /**
     * 字符常量：逗号 {@code ','}
     */
    char C_COMMA = CharConstants.COMMA;

    /**
     * 字符常量：花括号（左） <code>'{'</code>
     */
    char C_DELIM_START = CharConstants.DELIM_START;

    /**
     * 字符常量：花括号（右） <code>'}'</code>
     */
    char C_DELIM_END = CharConstants.DELIM_END;

    /**
     * 字符常量：中括号（左） {@code '['}
     */
    char C_BRACKET_START = CharConstants.BRACKET_START;

    /**
     * 字符常量：中括号（右） {@code ']'}
     */
    char C_BRACKET_END = CharConstants.BRACKET_END;

    /**
     * 字符常量：冒号 {@code ':'}
     */
    char C_COLON = CharConstants.COLON;

    /**
     * 字符常量：艾特 {@code '@'}
     */
    char C_AT = CharConstants.AT;

    /**
     * 字符串常量：制表符 {@code "\t"}
     */
    String TAB = "	";

    /**
     * 字符串常量：点 {@code "."}
     */
    String DOT = ".";

    /**
     * 字符串常量：双点 {@code ".."} <br>
     * 用途：作为指向上级文件夹的路径，如：{@code "../path"}
     */
    String DOUBLE_DOT = "..";

    /**
     * 字符串常量：斜杠 {@code "/"}
     */
    String SLASH = "/";

    /**
     * 字符串常量：反斜杠 {@code "\\"}
     */
    String BACKSLASH = "\\";

    /**
     * 字符串常量：回车符 {@code "\r"} <br>
     * 解释：该字符常用于表示 Linux 系统和 MacOS 系统下的文本换行
     */
    String CR = "\r";

    /**
     * 字符串常量：换行符 {@code "\n"}
     */
    String LF = "\n";

    /**
     * 字符串常量：Windows 换行 {@code "\r\n"} <br>
     * 解释：该字符串常用于表示 Windows 系统下的文本换行
     */
    String CRLF = "\r\n";

    /**
     * 字符串常量：下划线 {@code "_"}
     */
    String UNDERLINE = "_";

    /**
     * 字符串常量：减号（连接符） {@code "-"}
     */
    String DASHED = "-";

    /**
     * 字符串常量：逗号 {@code ","}
     */
    String COMMA = ",";

    /**
     * 字符串常量：花括号（左） <code>"{"</code>
     */
    String DELIM_START = "{";

    /**
     * 字符串常量：花括号（右） <code>"}"</code>
     */
    String DELIM_END = "}";

    /**
     * 字符串常量：中括号（左） {@code "["}
     */
    String BRACKET_START = "[";

    /**
     * 字符串常量：中括号（右） {@code "]"}
     */
    String BRACKET_END = "]";

    /**
     * 字符串常量：冒号 {@code ":"}
     */
    String COLON = ":";

    /**
     * 字符串常量：艾特 {@code "@"}
     */
    String AT = "@";


    /**
     * 字符串常量：XML 不间断空格转义 {@code "&nbsp;" -> " "}
     */
    String NBSP = "&nbsp;";

    /**
     * 字符串常量：XML And 符转义 {@code "&amp;" -> "&"}
     */
    String AMP = "&amp;";

    /**
     * 字符串常量：XML 双引号转义 {@code "&quot;" -> "\""}
     */
    String QUOTE = "&quot;";

    /**
     * 字符串常量：XML 单引号转义 {@code "&apos" -> "'"}
     */
    String APOS = "&apos;";

    /**
     * 字符串常量：XML 小于号转义 {@code "&lt;" -> "<"}
     */
    String LT = "&lt;";

    /**
     * 字符串常量：XML 大于号转义 {@code "&gt;" -> ">"}
     */
    String GT = "&gt;";

    /**
     * 字符串常量：空 JSON {@code "{}"}
     */
    String EMPTY_JSON = "{}";
}

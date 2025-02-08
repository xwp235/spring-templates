package jp.onehr.base.common.utils;

import java.util.ArrayList;
import java.util.List;

public class StrUtil {

    public static List<String> splitTrim(CharSequence str, char separator) {
        return split(str, separator, true, true);
    }

    public static List<String> split(CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
        if (null == str) {
            return new ArrayList<>(0);
        }
        int len = str.length();
        if (0 == len) {
            return new ArrayList<>(0);
        }
        List<String> list = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < len; i++) {
            if (str.charAt(i) == separator) {
                addSubString(str, start, i, isTrim, ignoreEmpty, list);
                start = i + 1;
            }
        }
        // 添加最后一个片段
        addSubString(str, start, len, isTrim, ignoreEmpty, list);
        return list;
    }

    private static void addSubString(CharSequence str, int start, int end, boolean isTrim, boolean ignoreEmpty, List<String> list) {
        String sub = (start == end) ? "" : str.subSequence(start, end).toString();
        if (isTrim) {
            sub = sub.trim();
        }
        if (!(ignoreEmpty && sub.isEmpty())) {
            list.add(sub);
        }
    }

}

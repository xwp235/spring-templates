package jp.onehr.base.common.utils;

import java.lang.reflect.Constructor;

public class ReflectUtil {

    // --------------------------------------------------------------------------------------------------------- Constructor

    /**
     * 获得一个类中所有构造列表，直接反射获取，无缓存
     *
     * @param beanClass 类
     * @return 字段列表
     */
    public static Constructor<?>[] getConstructorsDirectly(Class<?> beanClass) throws SecurityException {
        return beanClass.getDeclaredConstructors();
    }

    // --------------------------------------------------------------------------------------------------------- Field


}

package jp.onehr.base.common.utils;

import jp.onehr.base.common.exceptions.UtilException;

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

    // --------------------------------------------------------------------------------------------------------- newInstance

    /**
     * 实例化对象
     *
     * @param <T>   对象类型
     * @param clazz 类名
     * @return 对象
     * @throws UtilException 包装各类异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String clazz) throws UtilException {
        try {
            return (T) Class.forName(clazz).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new UtilException(e, "Instance class [{}] error!", clazz);
        }
    }

}

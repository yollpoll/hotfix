package com.yollpoll.hotfix;

import java.lang.reflect.Field;

/**
 * Created by spq on 2020/10/16
 */
public class ReflectUtils {
    /**
     * 获取属性值
     *
     * @param obj 对象
     * @param cls class
     * @param str 属性名称
     * @return 属性值
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getField(Object obj, Class cls, String str) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);//设置可访问
        return declaredField.get(obj);
    }

    /**
     * 设置属性值
     *
     * @param obj       对象
     * @param cls       class
     * @param fieldName 属性名
     * @param value     属性值
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setField(Object obj, Class cls, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cls.getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        declaredField.set(obj, value);
    }
}

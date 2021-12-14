package com.beyond.utils;

import java.lang.reflect.Method;

public class PojoUtil {
    /**
     * 根据字段名称取值
     *
     * @param obj       类名
     * @param fieldName 属性名
     * @return
     */

    public static Object getClassValue(Object obj, String fieldName) {

        if (obj == null) {

            return null;

        }

        try {

            Class beanClass = obj.getClass();

            Method[] ms = beanClass.getMethods();

            for (int i = 0; i < ms.length; i++) {

                // 非get方法不取

                if (!ms[i].getName().startsWith("get")) {

                    continue;

                }

                Object objValue = null;

                try {

                    objValue = ms[i].invoke(obj, new Object[]{});

                } catch (Exception e) {

                    continue;

                }

                if (objValue == null) {

                    continue;

                }

                if (ms[i].getName().toUpperCase().equals(fieldName.toUpperCase())

                        || ms[i].getName().substring(3).toUpperCase().equals(fieldName.toUpperCase())) {

                    return objValue;

                } else if (fieldName.toUpperCase().equals("SID") && (ms[i].getName().toUpperCase().equals("ID")

                        || ms[i].getName().substring(3).toUpperCase().equals("ID"))) {

                    return objValue;

                }

            }

        } catch (Exception e) {

        }

        return null;

    }
}

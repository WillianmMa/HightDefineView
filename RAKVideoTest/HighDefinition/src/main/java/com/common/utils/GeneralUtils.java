package com.common.utils;

import java.util.List;

/**
 * Created by david
 */
public class GeneralUtils {

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            if ("".equals(((String) object).trim()) || "null".equals((String) object)) {
                return true;
            } else {
                return false;
            }
        }

        if (object instanceof List) {
            return ((List) object).isEmpty();
        }

        return false;

    }


    /**
     * 判断对象是否为空
     *
     * @param o
     * @return
     */
    public static boolean isNull(Object o) {
        return o == null ? true : false;
    }

    /**
     * parameter 2 is contain in parameter 1.
     *
     * @param sourceFlag
     * @param compareFlag
     * @return
     */
    public static boolean isFlagContain(int sourceFlag, int compareFlag) {
        return (sourceFlag & compareFlag) == compareFlag;
    }
}

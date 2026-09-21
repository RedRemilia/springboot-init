package com.yupi.springbootinit.utils;

import java.util.Map;

public class MapUtils {

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}

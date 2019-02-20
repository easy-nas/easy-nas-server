package com.easynas.server.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 集合操作类
 *
 * @author liangyongrui
 */
public class CollectionUtils {
    private CollectionUtils() {
    }

    /**
     * 不修改list，返回一个新的list
     *
     * @param list    原list
     * @param element 增加的元素
     * @return 新list
     */
    public static <T> List<T> addList(List<T> list, T element) {
        final var result = new ArrayList<>(list);
        result.add(element);
        return result;
    }
}

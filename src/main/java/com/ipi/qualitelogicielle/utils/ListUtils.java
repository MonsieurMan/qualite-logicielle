package com.ipi.qualitelogicielle.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtils {
    private ListUtils() {

    }
    /**
     * Create a list containing given parameters
     *
     * @param items items to put in the list
     * @param <T>   item type
     * @return A new list with the given items
     */
    @SafeVarargs
    public static <T> List<T> of(T... items) {
        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list, items);
        return list;
    }
}

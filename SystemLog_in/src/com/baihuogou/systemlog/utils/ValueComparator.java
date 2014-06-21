package com.baihuogou.systemlog.utils;

import java.util.Comparator;
import java.util.Map;

public 	class ValueComparator implements Comparator<Map.Entry<String, Integer>> {
    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
        return o2.getValue() - o1.getValue();//这是降序，升序为：o1.getValue() - o2.getValue()
   }
}

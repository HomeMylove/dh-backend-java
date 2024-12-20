package com.homemylove.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtil {
    public static List<Long>  transformToList(Collection<Long> set){
        List<Long> ts = new ArrayList<>();
        for (Object f : set) {
            if(f instanceof Integer){
                ts.add(((Integer)f).longValue());
            }else {
                ts.add((Long) f);
            }
        }
        return ts;
    }

}

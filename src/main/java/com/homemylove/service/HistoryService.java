package com.homemylove.service;

import com.homemylove.core.cs.CS.REDIS_KEY;

import java.util.List;

public interface HistoryService {

    boolean saveHistory(REDIS_KEY prefix,String value,Long max);

    List<String> getHistory(REDIS_KEY prefix);

    boolean deleteHistory(REDIS_KEY prefix,String value);

}

package com.homemylove.service;

import com.homemylove.core.api.R;
import com.homemylove.core.entity.Query;
import com.homemylove.entity.ReadHistory;
import com.homemylove.vo.ListVo;
import com.homemylove.vo.ReadHistoryVo;

import java.util.List;

public interface ReadHistoryService {

    boolean saveReadHistory(ReadHistory readHistory);

    ListVo<ReadHistoryVo> getReadHistory(Query query,String keyword);

    R<String> deleteReadHistory(Long id);

    R<String> deleteAllReadHistory();

}

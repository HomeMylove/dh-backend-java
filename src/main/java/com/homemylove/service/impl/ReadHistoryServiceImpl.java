package com.homemylove.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.homemylove.auth.AuthInfo;
import com.homemylove.core.api.R;
import com.homemylove.core.entity.Query;
import com.homemylove.core.mdc.MDCScope;
import com.homemylove.entity.ReadHistory;
import com.homemylove.mapper.ReadHistoryMapper;
import com.homemylove.service.ArticleService;
import com.homemylove.service.ReadHistoryService;
import com.homemylove.utils.DateUtil;
import com.homemylove.vo.ArticleDetailVo;
import com.homemylove.vo.AuthorSearchVo;
import com.homemylove.vo.ListVo;
import com.homemylove.vo.ReadHistoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ReadHistoryServiceImpl implements ReadHistoryService {

    @Resource
    private ReadHistoryMapper readHistoryMapper;
    @Override
    public boolean saveReadHistory(ReadHistory readHistory) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return false;

        readHistory.setCreateTime(new Date());
        readHistory.setUserId(authInfo.getId());

        // 如果有先删除
        readHistoryMapper.delete(
                ReadHistory.gw().eq(ReadHistory::getUserId, readHistory.getUserId())
                        .eq(ReadHistory::getArticleId, readHistory.getArticleId())
        );
        return readHistoryMapper.insert(readHistory) == 1;
    }

    @Override
    public ListVo<ReadHistoryVo> getReadHistory(Query query,String keyword) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return null;
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<ReadHistoryVo> readHistoryVos = readHistoryMapper.getReadHistoryVoByUserId(authInfo.getId(), keyword);
        PageInfo<ReadHistoryVo> pageInfo = new PageInfo<>(readHistoryVos);
        return new ListVo<>(readHistoryVos, pageInfo.getTotal());
    }

    @Override
    public R<String> deleteReadHistory(Long id) {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return null;

        // 删除一条
        int delete = readHistoryMapper.delete(ReadHistory.gw()
                .eq(ReadHistory::getUserId, authInfo.getId())
                .eq(ReadHistory::getId, id));
        if (delete == 1) return R.success("删除成功");
        return R.fail("删除失败");
    }

    @Override
    public R<String> deleteAllReadHistory() {
        AuthInfo authInfo = MDCScope.getAuthInfo();
        if (authInfo == null) return null;

        // 删除一条
        int delete = readHistoryMapper.delete(ReadHistory.gw()
                .eq(ReadHistory::getUserId, authInfo.getId()));
        if (delete == 0) R.fail("删除失败");
        return R.success("删除成功");
    }
}

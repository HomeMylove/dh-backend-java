package com.homemylove.controller;

import com.homemylove.core.api.R;
import com.homemylove.core.cs.CS;
import com.homemylove.core.entity.Query;
import com.homemylove.entity.ReadHistory;
import com.homemylove.service.HistoryService;
import com.homemylove.service.ReadHistoryService;
import com.homemylove.vo.ListVo;
import com.homemylove.vo.ReadHistoryVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Resource
    private HistoryService historyService;

    @Resource
    private ReadHistoryService readHistoryService;

    /**
     * 保存搜索记录
     * @param choice
     * @param history
     * @return
     */
    @PostMapping("/search/{choice}/{history}")
    public R<Boolean> saveSearchHistory(@PathVariable("choice") String choice,
                                        @PathVariable("history") String history) {
        CS.REDIS_KEY prefix = "author".equals(choice) ?
                CS.REDIS_KEY.SEARCH_AUTHOR_HISTORY : CS.REDIS_KEY.SEARCH_ARTICLE_HISTORY;
        return R.data(historyService.saveHistory(prefix, history, 10L));
    }

    @GetMapping("/search/{choice}")
    public R<List<String>> getSearchHistory(@PathVariable("choice") String choice) {
        CS.REDIS_KEY prefix = "author".equals(choice) ?
                CS.REDIS_KEY.SEARCH_AUTHOR_HISTORY : CS.REDIS_KEY.SEARCH_ARTICLE_HISTORY;
        return R.data(historyService.getHistory(prefix));
    }

    @DeleteMapping("/search/{choice}/{history}")
    public R<String> deleteSearchHistory(@PathVariable("choice") String choice,
                                         @PathVariable("history") String history) {
        CS.REDIS_KEY prefix = "author".equals(choice) ?
                CS.REDIS_KEY.SEARCH_AUTHOR_HISTORY : CS.REDIS_KEY.SEARCH_ARTICLE_HISTORY;

        boolean deleted = historyService.deleteHistory(prefix, history);
        if (deleted) return R.success("删除成功");
        return R.fail("删除失败");
    }

    @PostMapping("/read")
    public R<Boolean> saveReadHistory(@RequestBody ReadHistory readHistory) {
        return R.data(readHistoryService.saveReadHistory(readHistory));
    }

    @GetMapping("/read/{pageNum}/{pageSize}")
    public R<ListVo<ReadHistoryVo>> getReadHistory(
            @RequestParam("keyword") @Nullable String keyword,
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("pageSize") Integer pageSize) {
        return R.data(readHistoryService.getReadHistory(new Query(pageNum, pageSize),keyword));
    }

    @DeleteMapping("/read/{id}")
    public R<String> deleteReadHistory(@PathVariable("id") Long id) {
        return readHistoryService.deleteReadHistory(id);
    }

    @DeleteMapping("/read")
    public R<String> deleteAllReadHistory() {
        return readHistoryService.deleteAllReadHistory();
    }
}

package com.homemylove.controller;

import com.homemylove.core.api.R;
import com.homemylove.core.entity.Query;
import com.homemylove.entity.Author;
import com.homemylove.service.AuthorService;
import com.homemylove.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/author")
@Slf4j
public class AuthorController {

    @Resource
    private AuthorService authorService;

    @PostMapping("/search/{keyword}")
    public R<ListVo<AuthorSearchVo>> search(@PathVariable("keyword") String keyword, @RequestBody Query query) {
        return R.data(authorService.getAuthorList(keyword, query));
    }

    @GetMapping("/hint/{hint}")
    public R<List<HintVo>> getAuthorHint(@PathVariable("hint") String hint) {
        return R.data(authorService.getAuthorHint(hint));
    }

    @GetMapping("/details/{id}")
    public R<AuthorDetailVo> detail(@PathVariable("id") Long id) {
        return R.data(authorService.getAuthorDetail(id));
    }

    @GetMapping("/check_author/{name}")
    public R<Boolean> checkAuthorName(@PathVariable("name") String name) {
        return R.data(authorService.checkAuthorName(name));
    }

    @PostMapping("/save")
    public R<String> saveAuthor(@RequestBody Author author) {
        return authorService.saveAuthor(author);
    }

    @GetMapping("/getId/{name}")
    public R<Long> getAuthorIdByName(@PathVariable("name") String name) {
        return R.data(authorService.getAuthorIdByName(name));
    }

    @GetMapping("/popularity")
    public R<List<Map<String,Object>>> getAuthorPopularity(){
        return R.data(authorService.getAuthorPopularity());
    }

    @PostMapping("/upload_history/{id}")
    public R<ListVo<UploadAuthorHistoryVo>> getUploadAuthorHistory(@PathVariable("id") Long createUser,
                                                                   @RequestBody Query query){
        return R.data(authorService.getUploadAuthorHistory(createUser,query));
    }

    @DeleteMapping("/{id}")
    public R<String> deleteAuthor(@PathVariable("id") Long id){
        return authorService.deleteAuthor(id);
    }


}

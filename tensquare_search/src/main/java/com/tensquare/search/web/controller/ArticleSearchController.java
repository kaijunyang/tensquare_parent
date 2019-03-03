package com.tensquare.search.web.controller;

import com.tensquare.search.po.Article;
import com.tensquare.search.service.ArticleSearchService;
import constants.StatusCode;
import dto.PageResultDTO;
import dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleSearchController {
    @Autowired
    private ArticleSearchService articleSearchService;

    @PostMapping
    public ResultDTO save(@RequestBody Article article) {
        articleSearchService.save(article);
        return new ResultDTO(true, StatusCode.OK, "操作成功");
    }

    @GetMapping("/search/{keywords}/{page}/{size}")
    public ResultDTO listByKeywordsLike(@PathVariable String keywords, @PathVariable int page, @PathVariable int size) {
        Page<Article> pageResponse = articleSearchService.findByContentOrTitleLike(keywords, page, size);
        return new ResultDTO(true, StatusCode.OK, "查询成功", new PageResultDTO<Article>(pageResponse.getTotalElements(), pageResponse.getContent()));
    }
}

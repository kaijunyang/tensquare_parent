package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleEsRepository;
import com.tensquare.search.po.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import utils.IdWorker;

@Service
public class ArticleSearchService {

    @Autowired
    private ArticleEsRepository articleEsRepository;
    @Autowired
    private IdWorker idWorker;

    public void save(Article article) {
        article.setId(idWorker.nextId()+"");
        articleEsRepository.save(article);
    }

    public Page<Article> findByContentOrTitleLike(String searchWord, int page, int size) {
        return articleEsRepository.findByTitleOrContentLike(searchWord, searchWord, PageRequest.of(page - 1, size));
    }
}

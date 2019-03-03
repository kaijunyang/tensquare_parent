package com.tensquare.search.dao;

import com.tensquare.search.po.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 文章搜索的持久层
 */
public interface ArticleEsRepository extends ElasticsearchRepository<Article,String> {

    /**
     * 根据标题或内容模糊查询分页列表
     * @param title
     * @param content
     * @param pageable
     * @return
     */
    Page<Article> findByTitleOrContentLike(String title, String content, Pageable pageable);

}

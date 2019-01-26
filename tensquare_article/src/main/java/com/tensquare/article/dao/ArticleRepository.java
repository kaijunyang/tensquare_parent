package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.po.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleRepository extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

    @Query("update Article set state = ?2 where id = ?1")
    @Modifying
    void examine(String articleId, String state);

    @Query("update Article set thumbup = thumbup + ?2 where id = ?1")
    @Modifying
    void thumbup(String articleId, int i);
}

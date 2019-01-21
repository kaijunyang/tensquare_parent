package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.po.Problem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.util.StringUtils;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemRepository extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    @Query("select p from Problem p where exists (select problemid from Pl where labelid = ?1 and problemid = p.id) order by p.replytime desc ")
    Page<Problem> getNewProblemList(String labelId, Pageable pageable);

    @Query("select p from Problem p where exists (select problemid from Pl where labelid = ?1 and problemid = p.id) order by p.reply desc ")
    Page<Problem> getHotList(String label, PageRequest pageRequest);

    @Query("select p from Problem p where exists (select problemid from Pl where labelid = ?1 and problemid = p.id) and p.reply = 0 order by p.createtime desc ")
    Page<Problem> getWaitList(String labelId, PageRequest pageRequest);
}

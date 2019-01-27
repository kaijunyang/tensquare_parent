package com.tensquare.spit.dao;

import com.tensquare.spit.po.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpitRepository extends MongoRepository<Spit, String> {
    Page<Spit> findByParentid(String parentid, Pageable pageable);
}

package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.po.User;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserRepository extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{
	
}

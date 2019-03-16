package com.tensquare.user.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;
import utils.IdWorker;

import com.tensquare.user.dao.UserRepository;
import com.tensquare.user.po.User;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private IdWorker idWorker;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

	/**
	 * 增加
	 * @param user
	 */
	public void saveUser(User user) {
		user.setId( idWorker.nextId()+"" );
		userRepository.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void updateUser(User user) {
		userRepository.save(user);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteUserById(String id) {
		userRepository.deleteById(id);
	}

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findUserList() {
		return userRepository.findAll();
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findUserById(String id) {
		return userRepository.findById(id).get();
	}

	/**
	 * 根据条件查询列表
	 * @param whereMap
	 * @return
	 */
	public List<User> findUserList(Map whereMap) {
		//构建Spec查询条件
        Specification<User> specification = getUserSpecification(whereMap);
		//Specification条件查询
		return userRepository.findAll(specification);
	}
	
	/**
	 * 组合条件分页查询
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findUserListPage(Map whereMap, int page, int size) {
		//构建Spec查询条件
        Specification<User> specification = getUserSpecification(whereMap);
		//构建请求的分页对象
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userRepository.findAll(specification, pageRequest);
	}

	/**
	 * 根据参数Map获取Spec条件对象
	 * @param searchMap
	 * @return
	 */
	private Specification<User> getUserSpecification(Map searchMap) {

		return (Specification<User>) (root, query, cb) ->{
				//临时存放条件结果的集合
				List<Predicate> predicateList = new ArrayList<Predicate>();
				//属性条件
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 登陆名
                if (searchMap.get("loginname")!=null && !"".equals(searchMap.get("loginname"))) {
                	predicateList.add(cb.like(root.get("loginname").as(String.class), "%"+(String)searchMap.get("loginname")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("image")!=null && !"".equals(searchMap.get("image"))) {
                	predicateList.add(cb.like(root.get("image").as(String.class), "%"+(String)searchMap.get("image")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
		
				//最后组合为and关系并返回
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));
		};

	}

	public void sendSms(String mobile) {
		// 生成随机六位数字为验证码
		Random random = new Random();
		int max = 999999;
		int min = 100000;
        int checkCode = random.nextInt(max); // 验证码
        if (checkCode < min) {
            checkCode += min;
        }

        // 存储在redis中以便验证
        redisTemplate.opsForValue().set("sms.checkcode."+mobile, checkCode+"", 5, TimeUnit.MINUTES);

        // rabbitmq
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("checkCode", checkCode);
        rabbitTemplate.convertAndSend("sms.checkcode", map);
    }

    public void register(User user, String checkcode) {

        String checkCodeRedis = redisTemplate.opsForValue().get("sms.checkcode." + user.getMobile());

        if (StringUtils.isEmpty(checkCodeRedis)) {
            throw new RuntimeException("请点击获取验证码");
        }
        // 验证验证码是否相同
        if (!checkCodeRedis.equals(checkcode)) {
            throw new RuntimeException("验证码输入错误");
        }

        user.setId( idWorker.nextId()+"" );
        user.setFollowcount(0);//关注数
        user.setFanscount(0);//粉丝数
        user.setOnline(0L);//在线时长
        user.setRegdate(new Date());//注册日期
        user.setUpdatedate(new Date());//更新日期

        userRepository.save(user);

        redisTemplate.delete("sms.checkcode." + user.getMobile());
    }
}

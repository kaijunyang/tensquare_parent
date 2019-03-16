package com.tensquare.user.web.controller;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.tensquare.user.po.User;
import com.tensquare.user.service.UserService;

import dto.PageResultDTO;
import dto.ResultDTO;
import constants.StatusCode;
import utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author BoBoLaoShi
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;
	
	
	/**
	 * 增加
	 * @param user
	 */
	@PostMapping
	public ResultDTO add(@RequestBody User user  ){
		userService.saveUser(user);
		return new ResultDTO(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@PutMapping("/{id}")
	public ResultDTO edit(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.updateUser(user);		
		return new ResultDTO(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public ResultDTO remove(HttpServletRequest request, @PathVariable String id ){
		// 鉴权
		String authorizationHeader = request.getHeader("JwtAuthorization");
		if (StringUtils.isEmpty(authorizationHeader)) {
			return new ResultDTO(true, StatusCode.OK, "权限不足1");
		}
		if (!authorizationHeader.startsWith("Bearer ")) {
			return new ResultDTO(true, StatusCode.OK, "权限不足2");
		}
		// 获取载荷
		Claims claims = null;
		try {
			String token = authorizationHeader.substring(7);
			claims = jwtUtils.parseJWT(token);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足3");
		}
		if (claims == null) {
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足4");
		}
		if (!"admin".equals(claims.get("admin"))) {
			return new ResultDTO(false,StatusCode.ACCESSERROR,"权限不足5");
		}

		// 删除成功
		userService.deleteUserById(id);
		return new ResultDTO(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public ResultDTO list(){
		return new ResultDTO(true,StatusCode.OK,"查询成功",userService.findUserList());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping("/{id}")
	public ResultDTO listById(@PathVariable String id){
		return new ResultDTO(true,StatusCode.OK,"查询成功",userService.findUserById(id));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list( @RequestBody Map searchMap){
        return new ResultDTO(true,StatusCode.OK,"查询成功",userService.findUserList(searchMap));
    }

	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@PostMapping("/search/{page}/{size}")
	public ResultDTO listPage(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageResponse = userService.findUserListPage(searchMap, page, size);
		return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<User>(pageResponse.getTotalElements(), pageResponse.getContent()) );
	}

	/**
	 * 发送手机验证码
	 * @param mobile
	 * @return
	 */
	@PostMapping("/sendsms/{mobile}")
	public ResultDTO sendSms(@PathVariable String mobile) {
		userService.sendSms(mobile);
		return new ResultDTO(true, StatusCode.OK, "发送成功");
	}

	/**
	 * 注册
	 */
	@PostMapping("/register/{checkcode}")
	public ResultDTO register(User user, @PathVariable String checkcode) {
		userService.register(user, checkcode);
		return new ResultDTO(true, StatusCode.OK, "请求成功");
	}

	/**
	 * 登录
	 */
	public ResultDTO login(String loginName, String password) {
		User user = userService.login(loginName, password);
		if (user == null) {
			return new ResultDTO(true, StatusCode.OK, "账号或者密码不正确");
		} else {
			return new ResultDTO(true, StatusCode.OK, "登录成功");
		}
	}
	
}

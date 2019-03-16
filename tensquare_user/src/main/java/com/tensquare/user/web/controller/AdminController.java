package com.tensquare.user.web.controller;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.user.po.Admin;
import com.tensquare.user.service.AdminService;

import dto.PageResultDTO;
import dto.ResultDTO;
import constants.StatusCode;
import utils.JwtUtils;

/**
 * 控制器层
 * @author BoBoLaoShi
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private JwtUtils jwtUtils;
	
	
	/**
	 * 增加
	 * @param admin
	 */
	@PostMapping
	public ResultDTO add(@RequestBody Admin admin  ){
		adminService.saveAdmin(admin);
		return new ResultDTO(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param admin
	 */
	@PutMapping("/{id}")
	public ResultDTO edit(@RequestBody Admin admin, @PathVariable String id ){
		admin.setId(id);
		adminService.updateAdmin(admin);		
		return new ResultDTO(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public ResultDTO remove(@PathVariable String id ){
		adminService.deleteAdminById(id);
		return new ResultDTO(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@GetMapping
	public ResultDTO list(){
		return new ResultDTO(true,StatusCode.OK,"查询成功",adminService.findAdminList());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@GetMapping("/{id}")
	public ResultDTO listById(@PathVariable String id){
		return new ResultDTO(true,StatusCode.OK,"查询成功",adminService.findAdminById(id));
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list( @RequestBody Map searchMap){
        return new ResultDTO(true,StatusCode.OK,"查询成功",adminService.findAdminList(searchMap));
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
		Page<Admin> pageResponse = adminService.findAdminListPage(searchMap, page, size);
		return  new ResultDTO(true,StatusCode.OK,"查询成功",  new PageResultDTO<Admin>(pageResponse.getTotalElements(), pageResponse.getContent()) );
	}

	@PostMapping("/login")
	@ResponseBody
	public ResultDTO login(Admin admin) {
		Admin admin1 = adminService.findAdminByLoginName(admin.getLoginname(), admin.getPassword());
		if (admin1 == null) {
			return new ResultDTO(true, StatusCode.OK, "账号或者密码错误");
		} else {
			// 登录成功,颁发token
			String token = jwtUtils.createJWT(admin.getId(), admin.getLoginname(), "admin");
			Map<String, Object> map = new HashMap<>();
			map.put("token", token);
			map.put("loginName", admin.getLoginname());
			return new ResultDTO(true, StatusCode.OK, "登录成功", map);
		}
	}
	
}

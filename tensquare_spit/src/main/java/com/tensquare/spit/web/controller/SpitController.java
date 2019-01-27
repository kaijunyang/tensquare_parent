package com.tensquare.spit.web.controller;

import com.tensquare.spit.po.Spit;
import com.tensquare.spit.service.SpitService;
import constants.StatusCode;
import dto.PageResultDTO;
import dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    @Autowired
    private RedisTemplate redisTemplate;

    private String thump_ = "thump_";

    /**
     * 增加
     *
     * @param spit
     */
    @PostMapping
    public ResultDTO add(@RequestBody Spit spit) {
        spitService.saveSpit(spit);
        return new ResultDTO(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param spit
     */
    @PutMapping("/{id}")
    public ResultDTO edit(@RequestBody Spit spit, @PathVariable String id) {
        spit.setId(id);
        spitService.updateSpit(spit);
        return new ResultDTO(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public ResultDTO remove(@PathVariable String id) {
        spitService.deleteSpitById(id);
        return new ResultDTO(true, StatusCode.OK, "删除成功");
    }

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public ResultDTO list() {
        return new ResultDTO(true, StatusCode.OK, "查询成功", spitService.findSpitList());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @GetMapping("/{id}")
    public ResultDTO listById(@PathVariable String id) {
        return new ResultDTO(true, StatusCode.OK, "查询成功", spitService.findSpitById(id));
    }

    /**
     * 根据上级ID查询吐槽分页数据
     *
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/comment/{parentid}/{page}/{size}")
    public ResultDTO listPageByParentid(@PathVariable String parentid, @PathVariable int page, @PathVariable int size) {
        Page<Spit> pageResponse = spitService.findSpitListPageByParentid(parentid, page, size);
        return new ResultDTO(true, StatusCode.OK, "查询成功", new PageResultDTO<Spit>(pageResponse.getTotalElements(), pageResponse.getContent()));
    }

    /**
     * 根据吐槽id，增加点赞的数量
     *
     * @param id
     * @return
     */
    @PutMapping("/thumbup/{id}")
    public ResultDTO incrementThumbup(@PathVariable String id) {
        String user = "123"; // todo: 暂时写死，等做完权限登录后再获取当前用户id

        // 验证redis里面是否存在该缓存
        String redisKey = thump_ + user + "_" + id;
        Object data = redisTemplate.opsForValue().get(redisKey);
        if (data == null) {
            // 允许点赞
            spitService.updateSpitThumbupToIncrementing(id);
            redisTemplate.opsForValue().set(redisKey, "1", 1L, TimeUnit.DAYS);
            return new ResultDTO(true, StatusCode.OK, "点赞成功");
        } else {
            return new ResultDTO(false, StatusCode.REPERROR, "今天已经点过赞了，明天再来");
        }
    }

}

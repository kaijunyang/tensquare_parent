package com.tensquare.qa.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.tensquare.qa.po.Problem;
import com.tensquare.qa.service.ProblemService;

import dto.PageResultDTO;
import dto.ResultDTO;
import constants.StatusCode;

/**
 * 控制器层
 *
 * @author BoBoLaoShi
 */
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;


    /**
     * 增加
     *
     * @param problem
     */
    @PostMapping
    public ResultDTO add(@RequestBody Problem problem) {
        problemService.saveProblem(problem);
        return new ResultDTO(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param problem
     */
    @PutMapping("/{id}")
    public ResultDTO edit(@RequestBody Problem problem, @PathVariable String id) {
        problem.setId(id);
        problemService.updateProblem(problem);
        return new ResultDTO(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public ResultDTO remove(@PathVariable String id) {
        problemService.deleteProblemById(id);
        return new ResultDTO(true, StatusCode.OK, "删除成功");
    }

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public ResultDTO list() {
        return new ResultDTO(true, StatusCode.OK, "查询成功", problemService.findProblemList());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @GetMapping("/{id}")
    public ResultDTO listById(@PathVariable String id) {
        return new ResultDTO(true, StatusCode.OK, "查询成功", problemService.findProblemById(id));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public ResultDTO list(@RequestBody Map searchMap) {
        return new ResultDTO(true, StatusCode.OK, "查询成功", problemService.findProblemList(searchMap));
    }

    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @PostMapping("/search/{page}/{size}")
    public ResultDTO listPage(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageResponse = problemService.findProblemListPage(searchMap, page, size);
        return new ResultDTO(true, StatusCode.OK, "查询成功", new PageResultDTO<Problem>(pageResponse.getTotalElements(), pageResponse.getContent()));
    }

    /**
     * 最新问答列表
     *
     * @param page  当前页
     * @param size
     * @param label id
     * @return
     */
    @GetMapping("/newlist/{label}/{page}/{size}")
    public ResultDTO newProblemList(@PathVariable int page, @PathVariable int size, @PathVariable String label) {
        Page<Problem> pageResponse = problemService.getNewProblemList(page, size, label);
        return new ResultDTO(true, StatusCode.OK, "查询成功", new PageResultDTO<Problem>(pageResponse.getTotalElements(), pageResponse.getContent()));
    }

    /**
     * 热门回答列表
     * @param label
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/hotlist/{label}/{page}/{size}")
    public ResultDTO hotList(@PathVariable String label, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageResponse = problemService.getHotList(label, page, size);
        return new ResultDTO(true, StatusCode.OK, "查询成功", new PageResultDTO<>(pageResponse.getTotalElements(), pageResponse.getContent()));
    }

    /**
     * 等待回答列表
     * @param label
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/waitlist/{label}/{page}/{size}")
    public ResultDTO waitList(@PathVariable String label, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageResponse = problemService.getWaitList(label, page, size);
        return new ResultDTO(true, StatusCode.OK, "查询成功", new PageResultDTO<>(pageResponse.getTotalElements(), pageResponse.getContent()));
    }
}

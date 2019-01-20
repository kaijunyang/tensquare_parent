package com.tensquare.base.web.controller;

import com.tensquare.base.po.Label;
import com.tensquare.base.service.LabelService;
import constants.StatusCode;
import dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin // 解决跨域
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @PostMapping
    public ResultDTO add(@RequestBody Label label) {
        labelService.saveLabel(label);
        return new ResultDTO(true, StatusCode.OK, "添加成功");
    }

    @GetMapping
    public ResultDTO list(){
//        int d = 1 / 0;
        List<Label> labelList = labelService.findLabelList();
        return new ResultDTO(true, StatusCode.OK, "查询成功", labelList);
    }

    @GetMapping("/{id}")
    public ResultDTO findById(@PathVariable String id) {
        Label label = labelService.findLabelById(id);
        return new ResultDTO(true, StatusCode.OK, "查询成功", label);
    }

    @PutMapping("/{id}")
    public ResultDTO edit(@PathVariable String id, @RequestBody  Label label) {
        label.setId(id);
        labelService.updateLabel(label);
        return new ResultDTO(true, StatusCode.OK, "修改成功");
    }

    @DeleteMapping("/{id}")
    public ResultDTO remove(@PathVariable String id) {
        labelService.deleteLabel(id);
        return new ResultDTO(true, StatusCode.OK, "删除成功");
    }

}

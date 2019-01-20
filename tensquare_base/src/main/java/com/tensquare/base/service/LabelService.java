package com.tensquare.base.service;

import com.tensquare.base.dao.LabelRepository;
import com.tensquare.base.po.Label;
import constants.StatusCode;
import dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.IdWorker;

import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private IdWorker idWorker;

    /**
     * 保存一个标签
     * @param label 标签
     * @return
     */
    public void saveLabel(Label label) {
        label.setId(idWorker.nextId() + "");
        labelRepository.save(label);
    }

    public void updateLabel(Label label) {
        labelRepository.save(label);
    }

    public void deleteLabel(String id) {
        labelRepository.deleteById(id);
    }

    public List<Label> findLabelList() {
        List<Label> labels = labelRepository.findAll();
        return labels;
    }

    public Label findLabelById(String id) {
        return labelRepository.findById(id).get();
    }
}

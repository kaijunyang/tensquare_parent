package com.tensquare.base.service;

import com.tensquare.base.dao.LabelRepository;
import com.tensquare.base.po.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utils.IdWorker;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private IdWorker idWorker;

    /**
     * 保存一个标签
     *
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

    public Page<Label> searchByPage(Map<String, Object> searchMap, Integer page, Integer size) {
        Specification<Label> labelSpecification = getLabelSpecification(searchMap);
        Pageable pageable = PageRequest.of(page - 1, size);
        return labelRepository.findAll(labelSpecification, pageable);
    }

    /**
     * 拼接查询条件
     *
     * @param searchMap
     * @return
     */
    private Specification<Label> getLabelSpecification(Map<String, Object> searchMap) {
        Specification<Label> specification = (Specification<Label>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            // 标签名称
            if (!StringUtils.isEmpty(searchMap.get("labelname"))) {
                predicateList.add(criteriaBuilder.like(root.get("labelname").as(String.class), "%" + searchMap.get("labelname") + "%"));
            }
            // 状态
            if (!StringUtils.isEmpty(searchMap.get("state"))) {
                predicateList.add(criteriaBuilder.equal(root.get("state").as(String.class), searchMap.get("state")));
            }
            // 是否推荐
            if (!StringUtils.isEmpty("recommend")) {
                predicateList.add(criteriaBuilder.equal(root.get("recommend").as(String.class), searchMap.get("recommend")));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };

        return specification;
    }

    public List<Label> search(Map<String, Object> searchMap) {
        Specification<Label> labelSpecification = getLabelSpecification(searchMap);
        return labelRepository.findAll(labelSpecification);
    }

}

package com.regent.rbp.api.service.bean.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.channel.ChannelOrganization;
import com.regent.rbp.api.dao.channel.ChannelOrganizationDao;
import com.regent.rbp.api.dto.base.ChannelOrganizationSaveParam;
import com.regent.rbp.api.dto.base.ChannelOrganizationSaveParamList;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.base.ChannelOrganizationService;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2021/12/16
 * @description
 */
@Service
public class ChannelOrganizationServiceBean implements ChannelOrganizationService {
    @Autowired
    ChannelOrganizationDao channelOrganizationDao;

    private DataResponse save(ChannelOrganization inputTree) {
        List<ChannelOrganization> all = channelOrganizationDao.selectList(new QueryWrapper<>());
        List<ChannelOrganization> rootTrees = constructTrees(all);

        boolean haveMerge = false;
        for (ChannelOrganization e : rootTrees) {
            if (StrUtil.equals(inputTree.getName(), e.getName())) {
                haveMerge = true;
                merge(e, inputTree);
                recursiveSave(e, 0);
            }
        }

        if (!haveMerge){
            if (inputTree.getId() == null) {
                inputTree.setParentId(0l);
                recursiveSave(inputTree, 0);
            }
        }

        return DataResponse.success();
    }

    private void recursiveSave(ChannelOrganization tree, int depth) {
        if (tree == null) return;
        if (tree.getId() == null) {
            tree.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            tree.setDepth(depth);
            channelOrganizationDao.insert(tree);
        }
        if (CollUtil.isNotEmpty(tree.getChildrenData())) {
            tree.getChildrenData().forEach(c -> {
                c.setParentId(tree.getId());
                recursiveSave(c, depth + 1);
            });
        }
    }

    private List<ChannelOrganization> constructTrees(List<ChannelOrganization> all) {
        Map<Long, ChannelOrganization> allMap = all.stream().collect(Collectors.toMap(ChannelOrganization::getId, Function.identity()));
        Map<Long, ChannelOrganization> sortAllMap = CollUtil.sort(allMap, (e1, e2) -> (int) (e1 - e2));
        sortAllMap.forEach((k, v) -> {
            ChannelOrganization p;
            if (v.getParentId() != null && (p = allMap.get(v.getParentId())) != null) {
                p.addChild(v);
                all.remove(v);
            }
        });
        return all;
    }

    private ChannelOrganization constructTree(ChannelOrganization container, ChannelOrganizationSaveParam rawData) {
        container.setName(rawData.getName());
        if (CollUtil.isNotEmpty(rawData.getChildrenData())) {
            List<ChannelOrganizationSaveParam> childrenData = rawData.getChildrenData();
            container.setChildrenData(new ArrayList<>(childrenData.size()));
            for (int i = 0; i < childrenData.size(); i++) {
                container.getChildrenData().add(constructTree(new ChannelOrganization(), childrenData.get(i)));
            }
        }
        return container;
    }

    @Override
    @Transactional
    public DataResponse save(ChannelOrganizationSaveParamList param) {
        for (ChannelOrganizationSaveParam e : param.getData()) {
            ChannelOrganization inputTree = constructTree(new ChannelOrganization(), e);
            save(inputTree);
        }
        return DataResponse.success();
    }

    @Override
    public PageDataResponse query() {
        List<ChannelOrganization> all = channelOrganizationDao.selectList(new QueryWrapper<ChannelOrganization>().orderByAsc("depth"));
        Map<Long, ChannelOrganization> resmap = new HashMap<>();
        all.forEach(e -> {
            resmap.put(e.getId(), e);
            Long parentId = e.getParentId();
            if (parentId != null && resmap.containsKey(parentId)) {
                List<ChannelOrganization> pcontainer = resmap.get(parentId).getChildrenData();
                if (pcontainer != null) {
                    pcontainer.add(e);
                } else {
                    ArrayList<ChannelOrganization> np = new ArrayList<>();
                    np.add(e);
                    resmap.get(parentId).setChildrenData(np);
                }
            }
        });

        PageDataResponse<ChannelOrganization> result = new PageDataResponse<>();
        List<ChannelOrganization> res = resmap.values().stream().filter(e -> e.getDepth() == 1).collect(Collectors.toList());
        result.setData(res);
        result.setTotalCount(res.size());
        return result;
    }

    private void merge(ChannelOrganization origin, ChannelOrganization input) {
        if (CollUtil.isEmpty(input.getChildrenData())) return;;
        if (StrUtil.equals(origin.getName(), input.getName())) {
            List<ChannelOrganization> child1 = origin.getChildrenData();
            List<ChannelOrganization> child2 = input.getChildrenData();
            Map<String, ChannelOrganization> child1Map = child1.stream().collect(Collectors.toMap(ChannelOrganization::getName, Function.identity()));
            Map<String, ChannelOrganization> child2Map = child2.stream().collect(Collectors.toMap(ChannelOrganization::getName, Function.identity()));
            child2Map.forEach((name, v) -> {
                ChannelOrganization child1Tree;
                if ((child1Tree = child1Map.get(name)) != null) {
                    merge(child1Tree, v);
                } else {
                    origin.addChild(v);
                }
            });
        }
    }

}
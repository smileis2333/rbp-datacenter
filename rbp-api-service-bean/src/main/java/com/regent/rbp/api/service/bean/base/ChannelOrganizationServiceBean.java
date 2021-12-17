package com.regent.rbp.api.service.bean.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.channel.ChannelOrganization;
import com.regent.rbp.api.dao.channel.ChannelOrganizationDao;
import com.regent.rbp.api.dto.base.ChannelOrganizationSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.base.ChannelOrganizationService;
import com.regent.rbp.api.service.base.context.ChannelOrganizationSaveContext;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    @Override
    @Transactional
    public DataResponse create(ChannelOrganizationSaveParam param) {
        ChannelOrganizationSaveContext context = new ChannelOrganizationSaveContext(param);
        List<String> errors = validateContext(context);
        if (!errors.isEmpty()) {
            String message = StringUtil.join(errors, ",");
            return DataResponse.errorParameter(message);
        }

        List<ChannelOrganization> cos = context.getCos();
        cos.forEach(channelOrganizationDao::insert);
        return DataResponse.success();
    }

    private List<String> validateContext(ChannelOrganizationSaveContext context) {
        ArrayList<String> errors = new ArrayList<>();

        List<ChannelOrganization> cos = context.getCos();
        if (cos.stream().map(ChannelOrganization::getName).filter(Objects::isNull).count() > 0) {
            errors.add("组织架构名全不能为空");
        }

        List<ChannelOrganization> exists = channelOrganizationDao.selectList(new QueryWrapper<ChannelOrganization>());
        Map<Long, ChannelOrganization> oldCOsMap = exists.stream().collect(Collectors.toMap(ChannelOrganization::getId, Function.identity()));

        // 全部组织架构的path mapping
        HashMap<String, ChannelOrganization> allPathMap = new HashMap<>();
        exists.stream().forEach(e -> {
            if (oldCOsMap.containsKey(e.getParentId())) {
                oldCOsMap.get(e.getParentId()).addChild(e);
            }
            allPathMap.put(getQuantifyPath(oldCOsMap, e), e);
        });
        // 终点路径的path mapping
        HashMap<String, ChannelOrganization> oldFinalPathMap = new HashMap<>();
        exists.stream().filter(e -> e.getChildrenData() == null).forEach(e -> {
            oldFinalPathMap.put(getQuantifyPath(oldCOsMap, e), e);
        });

        //请求的path mapping
        List<ChannelOrganization> inputs = context.getCos();
        Map<Long, ChannelOrganization> newCOsMap = inputs.stream().collect(Collectors.toMap(ChannelOrganization::getId, Function.identity()));
        HashMap<String, ChannelOrganization> newFinalPathMap = new HashMap<>();
        context.getCos().stream().filter(e -> e.getChildrenData() == null).forEach(e -> {
            newFinalPathMap.put(getQuantifyPath(newCOsMap, e), e);
        });

        ArrayList<ChannelOrganization> finalNewCos = new ArrayList<>();

        for (String b : newFinalPathMap.keySet()) {
            int maxSame = -1;
            ChannelOrganization maxSameItem = null;
            String maxSamePath = null;
            for (String a : oldFinalPathMap.keySet()) {
                String difference = StringUtils.difference(a, b);
                // 被包含
                if (difference.length() == 0) {
                    maxSame = 0;
                    break;
                } else if (!difference.startsWith("/")) {
                    // bias，/开头的的分割
                    difference = b.substring(0, b.replace(difference, "").lastIndexOf("/"));
                    difference = StringUtils.difference(difference, b);
                }

                int same = b.length() - difference.length();
                if (same < b.length() && same > maxSame) {
                    maxSame = same;
                    maxSamePath = StringUtils.remove(b, difference);
                    maxSameItem = allPathMap.get(maxSamePath);
                }
            }

            ChannelOrganization leaf = newFinalPathMap.get(b);
            if (maxSame != 0) {
                int count = (StrUtil.count(StringUtils.remove(b, maxSamePath), "/")) - 1;
                finalNewCos.add(leaf);

                ChannelOrganization middleRoot = leaf;
                while (count-- > 0) {
                    middleRoot = newCOsMap.get(middleRoot.getParentId());
                    finalNewCos.add(middleRoot);
                }
                middleRoot.setParentId(maxSameItem.getId());

            } else if (maxSame == -1) {
                // 加全部
                ChannelOrganization start = leaf;
                finalNewCos.add(leaf);
                while (newCOsMap.containsKey(start.getParentId())) {
                    start = newCOsMap.get(start.getParentId());
                    finalNewCos.add(start);
                }
            }
        }
        context.setCos(finalNewCos);
        return errors;

    }

    private String getQuantifyPath(Map<Long, ChannelOrganization> cosMap, ChannelOrganization co) {
        if (co == null)
            return "";
        return String.format("%s/%s", getQuantifyPath(cosMap, cosMap.get(co.getParentId())), co.getName());
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

}

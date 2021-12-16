package com.regent.rbp.api.service.base.context;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.core.channel.ChannelOrganization;
import com.regent.rbp.api.dto.base.ChannelOrganizationSaveParam;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2021/12/16
 * @description
 */
public class ChannelOrganizationSaveContext {
    private ChannelOrganizationSaveParam param;
    private ChannelOrganization root;
    private List<ChannelOrganization>cos;

    public ChannelOrganizationSaveContext(ChannelOrganizationSaveParam param) {
        this.param = param;
        this.root = convertCO(param);
        this.root.setParentId(0l);
        this.cos = new ArrayList<>();
        addCos(root,1);
    }

    private ChannelOrganization convertCO(ChannelOrganizationSaveParam param){
        ChannelOrganization co = new ChannelOrganization();
        co.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        co.setName(param.getName());
        if (CollUtil.isNotEmpty(param.getChildrenData())) {
            List<ChannelOrganization> cocs = param.getChildrenData().stream().map(e -> {
                ChannelOrganization coc = convertCO(e);
                coc.setParentId(co.getId());
                return coc;
            }).collect(Collectors.toList());
            co.setChildrenData(cocs);
        }
        return co;
    }

    private void addCos(ChannelOrganization root,int depth){
        cos.add(root);
        root.setDepth(depth);
        if (CollUtil.isNotEmpty(root.getChildrenData()))
        root.getChildrenData().forEach(e->addCos(e,depth+1));
    }

    public List<ChannelOrganization> getCos() {
        return cos;
    }

    public ChannelOrganization getRoot() {
        return root;
    }

    public void setCos(List<ChannelOrganization> cos) {
        this.cos = cos;
    }
}

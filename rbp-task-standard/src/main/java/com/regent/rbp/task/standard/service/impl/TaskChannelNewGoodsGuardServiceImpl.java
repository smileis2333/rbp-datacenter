package com.regent.rbp.task.standard.service.impl;

import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.sourcing.TaskChannelNewGoodsGuardDao;
import com.regent.rbp.api.dto.sourcing.NewGoodsGuardOptionDto;
import com.regent.rbp.api.dto.sourcing.TaskChannelNewGoodsGuardDto;
import com.regent.rbp.infrastructure.util.RedisUtil;
import com.regent.rbp.task.standard.service.TaskChannelNewGoodsGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 计算渠道新品保护
 * @author Moruins
 * @date 2022-03-28
 */
@Service
@RequiredArgsConstructor
public class TaskChannelNewGoodsGuardServiceImpl implements TaskChannelNewGoodsGuardService {
    final String SOURCING_OLD_GOODS = "RBP:SOURCING_OLD_GOODS";

    @Resource(name = "applicationTaskExecutor")
    private Executor executor;

    private final RedisUtil redisUtil;
    private final BaseDbDao baseDbDao;
    private final TaskChannelNewGoodsGuardDao mainDao;

    @Override
    public void runJob() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        futures.add(CompletableFuture.runAsync(this::run, executor));
        int size=futures.size();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[size])).join();
    }

    public void run(){
        String isCheckedSql = "select count(0) from rbp_sourcing_rule_options a " +
                "inner join rbp_sourcing_rule_bill b on a.bill_id=b.id " +
                "inner join rbp_base_sourcing_options c on a.options_id=c.id " +
                "where b.`status`=1 and c.notes='isNewGoodsGuardTime' ";

        int count=baseDbDao.getIntegerDataBySql(isCheckedSql);

        //存在开启了对应的新品保护业务选项的寻源规则才计算
        if(count>0){
            redisUtil.del(SOURCING_OLD_GOODS);
            List<NewGoodsGuardOptionDto> ruleList=mainDao.getRuleBill();
            Map<Long,List<String>> map=new HashMap<>();
            for (NewGoodsGuardOptionDto bill : ruleList) {
                //需要计算的渠道集合
                List<Long> channelIdList=mainDao.getWarehouseRange(bill.getBillId());
                //计算
                List<TaskChannelNewGoodsGuardDto> oldGoodsList=mainDao.getOldGoodsList(channelIdList,bill.getValue());

                Map<Long,List<TaskChannelNewGoodsGuardDto>> dataMap=oldGoodsList.stream().collect(Collectors.groupingBy(TaskChannelNewGoodsGuardDto::getChannelId));

                for (Long channelId : channelIdList) {
                    map.put(channelId,dataMap.get(channelId).stream().map(TaskChannelNewGoodsGuardDto::getSkuKey).collect(Collectors.toList()));
                }
            }
            //设置缓存
            redisUtil.set(SOURCING_OLD_GOODS,map);
        }
    }

}

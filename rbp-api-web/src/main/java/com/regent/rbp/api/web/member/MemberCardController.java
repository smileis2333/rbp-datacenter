package com.regent.rbp.api.web.member;

import com.regent.rbp.api.dto.channel.ChannelQueryParam;
import com.regent.rbp.api.dto.channel.ChannelQueryResult;
import com.regent.rbp.api.dto.channel.ChannelSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.member.MemberCardQueryParam;
import com.regent.rbp.api.dto.member.MemberCardQueryResult;
import com.regent.rbp.api.dto.member.MemberCardSaveParam;
import com.regent.rbp.api.service.member.MemberCardService;
import com.regent.rbp.api.web.constants.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 Controller
 * @author: HaiFeng
 * @create: 2021-09-14 11:42
 */
@RestController
@RequestMapping(ApiConstants.API_MEMBER_CARD)
public class MemberCardController {

    @Autowired
    MemberCardService memberCardService;

    @PostMapping("/query")
    public PageDataResponse<MemberCardQueryResult> query(@RequestBody MemberCardQueryParam param) {
        PageDataResponse<MemberCardQueryResult> result = memberCardService.query(param);
        return result;
    }

    @PostMapping("/save")
    public DataResponse save(@RequestBody MemberCardSaveParam param) {
        DataResponse result = memberCardService.save(param);
        return result;
    }

}
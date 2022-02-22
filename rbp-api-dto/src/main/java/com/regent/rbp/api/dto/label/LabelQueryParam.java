package com.regent.rbp.api.dto.label;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.DefaultParam;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
@Data
public class LabelQueryParam extends DefaultParam {
    private List<String> code;
    private List<String> goodsCode;
    private List<String> batchManagementNo;
    private List<String> channelCode;
    private List<Integer> status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDateEnd;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedDateEnd;
}
